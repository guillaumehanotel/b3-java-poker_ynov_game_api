package GameAPI.entities;

import GameAPI.entities.cards.Card;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Game {

    private static final Integer STARTING_CHIPS = 2000;
    public static final Integer NB_PLAYER_MAX = 2;

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
    @JsonIgnore
    public BlockingQueue<Game> actionQueue = new LinkedBlockingQueue<>();
    private Integer playingPlayerId;


    public Game() {
        incrementGameId();
        this.gameFlags = new ArrayList<>();
        this.gameStatus = GameStatus.STARTING_PENDING;
        this.players = new ArrayList<>();
        this.startingChips = STARTING_CHIPS;
        this.actionGuard = new ActionGuard();
        this.actionManager = new ActionManager(this);
        this.dealerPosition = 0;
        this.bigBlind = this.startingChips / 100;
        this.smallBlind = this.bigBlind / 2;
        this.rounds = new ArrayList<>();
        this.playingPlayerId = null;
    }

    private void incrementGameId() {
        Game.nbGame++;
        this.id = Game.nbGame;
    }

    public void addPlayer(User user) throws Exception {
        if (!checkIfUserIsAlreadyInGame(user)) {
            this.players.add(new Player(user, startingChips, this));
            log.info("Player [" + user.getUsername() + "] join the game n°" + this.getId());
            startGameIfAllPlayersHere();
        } else {
            throw new Exception("User " + user.getUsername() + " already in game " + this.getId() + " ! ");
        }
    }

    private boolean checkIfUserIsAlreadyInGame(User user) {
        return players.stream().map(Player::getUser).anyMatch(user1 -> user1.equals(user));
    }

    public void startGameIfAllPlayersHere() {
        if (this.players.size() == NB_PLAYER_MAX) {
            this.setGameStatus(GameStatus.IN_PROGRESS);
            new Thread(this::start).start();
        }
    }

    public void start() {
        log.info("[GAME " + id + "] START");
        this.gameFlags.add(GameFlag.GAME_STARTED);
        while (!gameHasWinner()) {
            Round round = new Round(this);
            this.rounds.add(round);
            round.start();
        }
        log.info("[GAME " + id + "] FINISHED");
        this.gameStatus = GameStatus.FINISHED;
    }

    private boolean gameHasWinner() {
        return getNonEliminatedPlayers().size() == 1;
    }

    @JsonIgnore
    public List<Player> getNonEliminatedPlayers() {
        return this.players.stream()
                .filter(player -> !player.getIsEliminated())
                .collect(Collectors.toList());
    }

    public void resetFlagAndQueue() {
        this.actionQueue.clear();
        this.joinQueue.clear();
        gameFlags.clear();
    }

    public User getUserById(Integer userId) {
        return getPlayerByUserId(userId).getUser();
    }

    public Player getPlayerByUserId(Integer userId){
        List<Player> resultUserList = this.players.stream()
                .filter(player -> player.getUser().getId().equals(userId))
                .collect(Collectors.toList());
        if (resultUserList.size() != 1)
            throw new IllegalStateException("No user found for this game or too much user found");
        return resultUserList.get(0);
    }

    public void addFlag(GameFlag gameFlag) {
        this.gameFlags.add(gameFlag);
    }

    @Override
    public String toString() {
        return "Game{" + gameStatus +
                ", players=" + players +
                '}';
    }

    List<Card> getLastCommunityCards() {
        return rounds.get(rounds.size() - 1).getCommunityCards();
    }
}
