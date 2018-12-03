package GameAPI.controllers;

import GameAPI.engine.action.Action;
import GameAPI.engine.card.Card;
import GameAPI.engine.game.Game;
import GameAPI.engine.game.GameStatus;
import GameAPI.engine.game.GameSystem;
import GameAPI.engine.user.User;
import GameAPI.engine.user.UserCards;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameSystem gameSystem;

    @RequestMapping(value = "/game/startingChips", method = RequestMethod.GET)
    Integer getStartingChips() {
        return GameSystem.STARTING_CHIPS;
    }

    @RequestMapping(value = "/users/join", method = RequestMethod.POST)
    @ResponseBody
    Game userJoinGame(@RequestBody User user) throws InterruptedException {
        Game game = gameSystem.userAskForGame(user);
        game.resetFlagAndQueueAndErrors();
        return game.getGameStatus() == GameStatus.IN_PROGRESS ? game.joinQueue.take() : game;
    }

    /**
     * Gestion de la réception d'une action
     * Vérifie que l'action possède bien un gameId, et récupère le Game associé.
     * A partir de ce game, aller checker son ActionGuard pour vérifier :
     * - si une action est attendue
     * - si le user à l'origine de cette action est bien celui attendu
     * Si tout est ok, alors on passe l'action au service qui va l'éxecuter
     */
    @RequestMapping(value = "/action", method = RequestMethod.POST)
    Game handleAction(@RequestBody Action action) {
        Integer gameId = action.getGameId();
        Game game = null;
        try {
            game = gameSystem.getGameById(gameId);

            if (action.isValid()) {

                log.info("[ACTION RECEIVED : " + action.toString() + "]");

                User user = game.getUserById(action.getUserId());
                game.resetFlagAndQueueAndErrors();

                if (game.waitsActionFromUser(user)) {
                    game.getActionManager().registerAction(action);
                } else {
                    game.markActionAsProcessed();
                }

            } else {
                game.addError("[ACTION REJECTED]");
                game.markActionAsProcessed();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return game != null ? game.returnGameWhenActionProcessed() : null;
    }

    @RequestMapping(value = "/game/{gameId}/users/{userId}/cards", method = RequestMethod.GET)
    List<Card> getUserCards(@PathVariable Integer gameId, @PathVariable Integer userId) {
        Game game = gameSystem.getGameById(gameId);
        return game.getPlayerByUserId(userId).getDownCards();
    }

    @RequestMapping(value = "/game/{gameId}/users/previous/cards", method = RequestMethod.GET)
    List<UserCards> getPreviousUsersDowncards(@PathVariable Integer gameId) {
        Game game = gameSystem.getGameById(gameId);
        return game.getPreviousUsersDowncards();
    }

    @RequestMapping(value = "/game/{gameId}/previous/communitycards")
    List<Card> getPreviousCommunityCards(@PathVariable Integer gameId) {
        Game game = gameSystem.getGameById(gameId);
        return game.getLastCommunityCards();
    }
}
