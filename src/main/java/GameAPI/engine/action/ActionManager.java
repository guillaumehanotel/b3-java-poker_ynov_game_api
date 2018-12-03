package GameAPI.engine.action;

import GameAPI.engine.game.Game;
import GameAPI.engine.user.Player;
import GameAPI.engine.game.Round;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe appelée lorsqu'une action est reçue et acceptée pour une partie donnée
 * Elle va générer une méthode à faire en fonction du type d'action reçu
 * C'est cette classe qui va éxecuter l'action d'un joueur lorsqu'une action est attendu
 */
@Slf4j
public class ActionManager {

    private Game game;

    private ActionType action;
    private Integer actionParameter;

    public ActionManager(Game game) {
        this.game = game;
        this.action = null;
        this.actionParameter = null;
    }

    /**
     * Quand une action est attendu, méthode appelé continuellement pour vérifier qu'il y a une action à faire
     */
    public Boolean playActionIfExist(Round round) {
        if (action != null) {
            this.playAction(round);
        }
        return true;
    }

    /**
     * Execution de la méthode provenant d'une requete
     */
    private void playAction(Round round) {
        executeAction(round);
        deregisterAction();

        // Si le tour n'est pas fini, le jeu ne peut pas plus avancer à ce point et on peut retourner le jeu pour attendre la prochaine action
        if (!round.getCurrentTurn().isFinished()){
            game.markActionAsProcessed();
            round.passToNextPlayerAndWaitAction();
        }

    }

    private void executeAction(Round round) {
        Player player = round.getPlayers().getPlayingPlayer();

        switch (action) {
            case BET:
                player.bets(actionParameter);
                break;
            case CALL:
                player.call(round.getBiggestBet());
                break;
            case FOLD:
                player.fold();
                break;
        }

    }

    public void registerAction(Action action) {
        log.info("[ACTION SAVED]");
        this.action = action.getActionType();
        this.actionParameter = action.getValue();
    }

    private void deregisterAction() {
        this.action = null;
        this.actionParameter = null;
    }

}
