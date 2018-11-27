package GameAPI.engine.game;

import GameAPI.engine.card.combinations.*;
import GameAPI.engine.user.Player;
import GameAPI.engine.user.User;
import GameAPI.engine.action.ActionGuard;
import GameAPI.engine.action.ActionManager;
import GameAPI.engine.card.Card;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Game {

    static final Integer NB_PLAYER_MAX = 2;

    private static Integer nbGame = 0;

    private Integer id;
    private List<GameFlag> gameFlags;
    private GameStatus gameStatus;
    private List<Player> players;
    private Integer startingChips;
    @JsonIgnore
    private ActionGuard actionGuard;
    @JsonIgnore
    private ActionManager actionManager;
    private Integer dealerPosition;
    private List<Round> rounds;
    private Integer smallBlind;
    private Integer bigBlind;
    @JsonIgnore
    public BlockingQueue<Game> joinQueue = new LinkedBlockingQueue<>();
    /**
     * This field is used as a way to get this when it is ready.
     */
    @JsonIgnore
    private BlockingQueue<Game> gameQueue = new LinkedBlockingQueue<>();

    private Integer playingPlayerId;
    private Integer playingPlayerCallValue;
    private Integer pot;
    private Integer winnerId;
    private List<String> errors;
    @JsonIgnore
    private final List<Class<? extends Combination>> combinationTypes = Arrays.asList(
        HighHand.class,
        Pair.class,
        DoublePair.class,
        ThreeOfAKind.class,
        Straight.class,
        Flush.class,
        FullHouse.class,
        FourOfAKind.class,
        StraightFlush.class,
        RoyalFlush.class
    );

    public Game() {
        this.id = ++Game.nbGame;
        this.gameFlags = new ArrayList<>();
        this.gameStatus = GameStatus.STARTING_PENDING;
        this.players = new ArrayList<>();
        this.startingChips = GameSystem.STARTING_CHIPS;
        this.actionGuard = new ActionGuard();
        this.actionManager = new ActionManager(this);
        this.dealerPosition = 0;
        this.bigBlind = this.startingChips / 100;
        this.smallBlind = this.bigBlind / 2;
        this.rounds = new ArrayList<>();
        this.playingPlayerId = null;
        this.playingPlayerCallValue = null;
        this.pot = 0;
        this.winnerId = null;
        this.errors = new ArrayList<>();
    }

    void addPlayer(User user) throws Exception {
        if (!checkIfUserIsAlreadyInGame(user)) {
            this.players.add(new Player(user, startingChips, this));
            user.setMoney(user.getMoney() - startingChips);
            log.info("Player [" + user.getUsername() + "] join the game n°" + this.getId());
            startGameIfAllPlayersHere();
        } else {
            throw new Exception("User " + user.getUsername() + " already in game " + this.getId() + " ! ");
        }
    }

    private Boolean checkIfUserIsAlreadyInGame(User user) {
        return players.stream().map(Player::getUser).anyMatch(user1 -> user1.equals(user));
    }

    private void startGameIfAllPlayersHere() {
        if (this.players.size() == NB_PLAYER_MAX) {
            this.setGameStatus(GameStatus.IN_PROGRESS);
            new Thread(this::start).start();
        }
    }

    private void start() {
        log.info("[GAME " + id + "] START");
        this.gameFlags.add(GameFlag.GAME_STARTED);
        while (!gameHasWinner()) {
            this.setPot(0);
            Round round = new Round(this);
            this.rounds.add(round);
            round.start();
        }
        log.info("[GAME " + id + "] FINISHED");
        this.gameStatus = GameStatus.FINISHED;
        this.winnerId = getWinner().getId();
        this.markActionAsProcessed();
    }

    private Boolean gameHasWinner() {
        return getNonEliminatedPlayers().size() == 1;
    }

    private User getWinner(){
        if(getNonEliminatedPlayers().size() == 0 || getNonEliminatedPlayers().size() > 1){
            throw new IllegalStateException("Cannot get winner for the game " + this.id);
        }
        return getNonEliminatedPlayers().get(0).getUser();
    }

    @JsonIgnore
    public List<Player> getNonEliminatedPlayers() {
        return this.players.stream()
                .filter(player -> !player.getIsEliminated())
                .collect(Collectors.toList());
    }

    public void resetFlagAndQueueAndErrors() {
        this.errors.clear();
        this.gameQueue.clear();
        this.joinQueue.clear();
        gameFlags.clear();
    }

    public User getUserById(Integer userId) {
        return getPlayerByUserId(userId).getUser();
    }

    public Player getPlayerByUserId(Integer userId){
        List<Player> resultUserList = players.stream()
                .filter(player -> player.getUser().getId().equals(userId))
                .collect(Collectors.toList());
        if (resultUserList.size() != 1)
            throw new IllegalStateException("No user found for this game or too much user found");
        return resultUserList.get(0);
    }

    void addFlag(GameFlag gameFlag) {
        gameFlags.add(gameFlag);
    }

    public void addError(String error){
        this.errors.add(error);
        log.error(error);
    }

    public void updatePlayingPlayerData(Round round){
        Player playingPlayer = round.getPlayers().getPlayingPlayer();
        this.setPlayingPlayerId(playingPlayer.getUser().getId());
        this.setPlayingPlayerCallValue(round.getBiggestBet() - playingPlayer.getCurrentBet());
    }

    /**
     * When this method is called, we consider this has ended to process the last action it had to process
     */
    public void markActionAsProcessed(){
        try {
            gameQueue.put(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return this when this has ended to process an action
     */
    public Game returnWhenActionProcessed() {
        try {
            return gameQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Game{" + gameStatus + ", players=" + players + '}';
    }

    @JsonIgnore
    public List<Card> getLastCommunityCards() {
        return rounds.isEmpty() ? new ArrayList<>() : rounds.get(rounds.size() - 1).getCommunityCards();
    }

    public List<Class<? extends Combination>> getCombinationTypes() {
        return combinationTypes;
    }

    /**
     * Vérifie d'une action est attendue
     */
    public Boolean isActionExpected() {
        ActionGuard actionGuard = getActionGuard();
        if (actionGuard.getAnActionIsExpected()) {
            return true;
        } else {
            addError("The game doesn't wait an action");
            return false;
        }
    }

    /**
     * Vérifie que le user à l'origine de l'action est bien attendue
     */
    public Boolean isUserValid(User user) {
        ActionGuard actionGuard = getActionGuard();
        if (actionGuard.getUserId().equals(user.getId())) {
            return true;
        } else {
            addError("Action forbidden for User n°" + user.getId() + " : " + user.getUsername());
            return false;
        }
    }

    public Boolean waitsActionFromUser(User user) {
        return isActionExpected() && isUserValid(user);
    }
}
