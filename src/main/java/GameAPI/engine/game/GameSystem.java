package GameAPI.engine.game;

import GameAPI.engine.user.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class in charge of creating games according to user requests
 */
@Component
@Slf4j
@Data
public class GameSystem {

    private List<Game> games;
    public static final Integer STARTING_CHIPS = 2000;

    public GameSystem() {
        this.games = new ArrayList<>();
    }

    /**
     * Method in charge of finding a game for a user
     * @param user The user who asks to join a game
     * @return The game found
     */
    public Game userJoinGame(User user) {
        Game game = this.findGameForUser();
        try {
            game.addPlayer(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            game.addError(e.getMessage());
        }
        return game;
    }

    /**
     * Create a game if none exists
     * Otherwise create a game if there is no pending game
     * If there are games waiting, join the first one
     * @return The game found
     */
    private Game findGameForUser() {
        Game game;
        if (this.games.isEmpty()) {
            game = new Game();
            this.games.add(game);
        } else {
            List<Game> waitingGames = getWaitingGames();
            if (waitingGames.size() == 0) {
                game = new Game();
                this.games.add(game);
            } else {
                game = waitingGames.get(0);
            }
        }
        return game;
    }

    /**
     * @return List of pending games
     */
    private List<Game> getWaitingGames() {
        return games.stream()
                .filter(game -> game.getGameStatus() == GameStatus.STARTING_PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Go search among the list of all the games, the one whose ID corresponds to the one passed in parameter
     * Raises an exception if none or more are found
     * @param id The game id
     * @return The game found
     */
    public Game getGameById(Integer id) {
        List<Game> resultGameList = this.games.stream()
                .filter(game -> game.getId().equals(id))
                .collect(Collectors.toList());
        if (resultGameList.size() != 1)
            throw new IllegalStateException("No game or too much game found");
        return resultGameList.get(0);
    }

}
