package GameAPI.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Game {

    private static final Integer STARTING_CHIPS = 2000;
    private static final Integer NB_PLAYER_MAX = 4;

    private static Integer nbGame = 0;

    private Integer id;
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

    public Game() {
        incrementGameId();
        this.gameStatus = GameStatus.STARTING_PENDING;
        this.players = new ArrayList<>();
        this.startingChips = STARTING_CHIPS;
        this.actionGuard = new ActionGuard();
        this.actionManager = new ActionManager(this);
        this.dealerPosition = 0;
        this.bigBlind = this.startingChips / 100;
        this.smallBlind = this.bigBlind / 2;
        this.rounds = new ArrayList<>();
    }

    private void incrementGameId() {
        Game.nbGame++;
        this.id = Game.nbGame;
    }

    public void addPlayer(User user) throws Exception {
        if (!checkIfUserIsAlreadyInGame(user)) {
            this.players.add(new Player(user, startingChips));
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
            GameResponse.getInstance().addGame(this);
            new Thread(this::start).start();
        }
    }

    public void start() {
        log.info("start GAME " + id);
        this.setGameStatus(GameStatus.IN_PROGRESS);
        while (!gameHasWinner()) {
            this.rounds.add(new Round(this));
        }
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

    public void incrementDealerPosition() {
        if (this.dealerPosition + 1 > getNonEliminatedPlayers().size() - 1) {
            this.dealerPosition = 0;
        } else {
            this.dealerPosition++;
        }
    }

    @Override
    public String toString() {
        return "Game{" + gameStatus +
                ", players=" + players +
                '}';
    }
}
