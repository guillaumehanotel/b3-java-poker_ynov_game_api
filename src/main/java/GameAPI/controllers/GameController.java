package GameAPI.controllers;

import GameAPI.entities.*;
import GameAPI.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    private GameService gameService;

    /**
     * Un user demande à rejoindre une partie
     */
    @RequestMapping(value = "/users/{id}/join", method = RequestMethod.GET)
    @ResponseBody Game userJoinGame(@PathVariable Long id) {
        return gameService.makeUserJoinAGame(id);
    }

    /**
     * Gestion de la réception d'une action
     * Vérifie que l'action possède bien un gameId, et récupère le Game associé.
     *
     * A partir de ce game, aller checker son ActionGuard pour vérifier :
     * - si une action est attendue
     * - si le user à l'origine de cette action est bien celui attendu
     * Si tout est ok, alors on passe l'action au service qui va l'éxecuter
     */
    @RequestMapping(value = "/action", method = RequestMethod.POST)
    Game handleAction(@RequestBody Action action) {
        Game game = null;

        if (checkAction(action)) {

            log.info("ACTION RECEIVED : " + action.toString());

            Integer gameId = action.getGameId();
            Long userId = action.getUserId().longValue();

            try {
                User user = gameService.getUserService().getOneById(userId);
                game = gameService.getGameSystem().getGameById(gameId);
                if (checkGameActionGuard(game, user)) {
                    game.getActionManager().saveAction(action);
                }

            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return game;
    }

    /**
     * Vérifie qu'une action reçue possède bien les champs requis
     */
    private boolean checkAction(Action action) {
        boolean check = true;
        if(action.getGameId() == null){
            check = false;
            log.error("Missing gameId of Action");
        }
        if(action.getUserId() == null){
            check = false;
            log.error("Missing userId of Action");
        }
        if(action.getActionType() == null){
            check = false;
            log.error("Missing actionType of Action");
        } else {
            check = this.checkActionTypeParameter(action);
        }
        return check;
    }

    /**
     * Vérifie qu'une action possède bien une valeur dans le cas où l'action serait BET ou RAISE
     */
    private boolean checkActionTypeParameter(Action action) {
        boolean check = true;
        if(action.getActionType() == ActionType.BET || action.getActionType() == ActionType.RAISE){
            if(action.getValue() == null){
                check = false;
                log.error("Missing value for action BET or RAISE");
            }
        }
        return check;
    }

    /**
     * Récupère le système de protection du jeu passé en paramètre vérifie si une action est attendue et
     * si le user à l'origine de l'action est valide
     */
    private boolean checkGameActionGuard(Game game, User user) {
        ActionGuard actionGuard = game.getActionGuard();
        return isActionExpected(actionGuard) && isUserValid(actionGuard, user);
    }

    /**
     * Vérifie que le user à l'origine de l'action est bien attendue
     */
    private boolean isUserValid(ActionGuard actionGuard, User user) {
        if(actionGuard.getUserId().equals(user.getId())){
            return true;
        } else {
            log.error("Action forbidden for User n°" + user.getId() + " : " + user.getUsername());
            return false;
        }
    }

    /**
     * Vérifie d'une action est attendue
     */
    private boolean isActionExpected(ActionGuard actionGuard) {
        if(actionGuard.getActionExpected()){
            return true;
        } else {
            log.error("The game doesn't wait an action");
            return false;
        }
    }


}
