package GameAPI.controllers;

import GameAPI.engine.action.Action;
import GameAPI.engine.action.ActionGuard;
import GameAPI.engine.action.ActionType;
import GameAPI.engine.card.Card;
import GameAPI.engine.game.Game;
import GameAPI.engine.game.GameStatus;
import GameAPI.engine.game.GameSystem;
import GameAPI.engine.game.Result;
import GameAPI.engine.user.Player;
import GameAPI.engine.user.User;
import GameAPI.engine.user.UserCards;
import GameAPI.services.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Controller qui a pour but de recevoir les actions qui viennent du front
 * et d'éxecuter l'action passé dans le body en vérifiant que :
 * 1- une action est bien attendu
 * 2- le user à l'origine de l'action est bien celui qui doit jouer
 */
@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameSystem gameSystem;

    @Autowired
    private StatsService statsService;

    @RequestMapping(value = "/users/{userId}/stats", method = RequestMethod.GET)
    ResponseEntity<Result> getUserStats(@PathVariable Integer userId) {
        return new ResponseEntity<>(statsService.getResultsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Get starting chips
     */
    @RequestMapping(value = "/game/startingChips", method = RequestMethod.GET)
    Integer getStartingChips() {
        return GameSystem.STARTING_CHIPS;
    }


    /**
     * Un user demande à rejoindre une partie
     */
    @RequestMapping(value = "/users/join", method = RequestMethod.POST)
    @ResponseBody
    Game userJoinGame(@RequestBody User user) throws InterruptedException {
        Game game = gameSystem.userAskForGame(user);
        user.setResult(statsService.getResultsByUserId(user.getId()));
        game.resetFlagAndQueueAndErrors();
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            return game.joinQueue.take();
        } else {
            return game;
        }
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
    Game handleAction(@RequestBody Action action) throws InterruptedException {
        Integer gameId = action.getGameId();
        Game game = null;
        try {
            game = gameSystem.getGameById(gameId);

            if (checkAction(action)) {

                log.info("[ACTION RECEIVED : " + action.toString() + "]");

                User user = game.getUserById(action.getUserId());
                game.resetFlagAndQueueAndErrors();

                if (checkGameActionGuard(game, user)) {
                    game.getActionManager().saveAction(action);
                } else {

                    game.markActionAsProcessed();
                }

            } else {
                game.addError("INVALID ACTION");
                game.markActionAsProcessed();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return game != null ? game.returnActionWhenProcessed() : null;
    }

    @RequestMapping(value = "/game/{gameId}/users/{userId}/cards", method = RequestMethod.GET)
    List<Card> getUserCards(@PathVariable Integer gameId, @PathVariable Integer userId) {
        Game game = gameSystem.getGameById(gameId);
        return game.getPlayerByUserId(userId).getDownCards();
    }

    @RequestMapping(value = "/game/{gameId}/users/previous/cards", method = RequestMethod.GET)
    List<UserCards> getPreviousUsersDowncards(@PathVariable Integer gameId) {
        List<UserCards> userCards = new ArrayList<>();
        Game game = gameSystem.getGameById(gameId);
        for (Player player : game.getNonEliminatedPlayers()) {
            userCards.add(new UserCards(player.getUser().getId(), player.getPreviousDownCards()));
        }
        return userCards;
    }

    /**
     * Vérifie que'une action reçue possède bien les champs requis
     */
    private Boolean checkAction(Action action) {
        Boolean check = true;
        if (action.getGameId() == null) {
            check = false;
            log.error("Missing gameId of Action");
        }
        if (action.getUserId() == null) {
            check = false;
            log.error("Missing userId of Action");
        }
        if (action.getActionType() == null) {
            check = false;
            log.error("Missing actionType of Action");
        } else {
            check = this.checkActionTypeParameter(action);
        }
        return check;
    }

    /**
     * Vérifie que'une action possède bien une valeur dans le cas où l'action serait BET
     */
    private Boolean checkActionTypeParameter(Action action) {
        Boolean check = true;
        if (action.getActionType() == ActionType.BET) {
            if (action.getValue() == null) {
                check = false;
                log.error("Missing value for action BET");
            }
        }
        return check;
    }

    /**
     * Récupère le système de protection du jeu passé en paramètre vérifie si une action est attendue et
     * si le user à l'origine de l'action est valide
     */
    private Boolean checkGameActionGuard(Game game, User user) {
        return isActionExpected(game) && isUserValid(game, user);
    }

    /**
     * Vérifie que le user à l'origine de l'action est bien attendue
     */
    private Boolean isUserValid(Game game, User user) {
        ActionGuard actionGuard = game.getActionGuard();
        if (actionGuard.getUserId().equals(user.getId())) {
            return true;
        } else {
            game.addError("Action forbidden for User n°" + user.getId() + " : " + user.getUsername());
            return false;
        }
    }

    /**
     * Vérifie d'une action est attendue
     */
    private Boolean isActionExpected(Game game) {
        ActionGuard actionGuard = game.getActionGuard();
        if (actionGuard.getActionExpected()) {
            return true;
        } else {
            game.addError("The game doesn't wait an action");
            return false;
        }
    }


}
