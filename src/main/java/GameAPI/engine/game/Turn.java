package GameAPI.engine.game;

import GameAPI.engine.user.Player;
import GameAPI.engine.user.Players;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class Turn {

    private Game game;
    private Round round;
    private Players players;
    private TurnPhase turnPhase;

    private final HashMap<TurnPhase, Integer> turnCommunityCardsMap = new HashMap<TurnPhase, Integer>(){{
        put(TurnPhase.PREFLOP, 0);
        put(TurnPhase.FLOP, 3);
        put(TurnPhase.TURN, 1);
        put(TurnPhase.RIVER, 1);
    }};

    public Turn(Round round, TurnPhase turnPhase) {
        this.round = round;
        this.turnPhase = turnPhase;
        this.game = round.getGame();
        this.players = round.getPlayers();
    }

    /**
     * Lancement d'un tour de mise en admettant que les cartes aient été distribuées et que les blinds aient été misées
     * Un tour de mise continue tant que :
     * - tout le monde n'a pas joué 1 fois
     * - tout le monde (pas couché) n'a pas la même mise
     */
    public void play() {
        log.info("[TURN] START " + turnPhase);
        game.addFlag(GameFlag.NEW_TURN);
        initTurn();

        round.passToNextPlayerAndWaitAction();

        // Retourne le jeu si tous les joueurs sont présents
        game.markLastJoinAsProcessed();

        // Si le tour n'est pas fini, on retourne le jeu pour attendre la prochaine action
        if (!isFinished())
            game.markActionAsProcessed();

        while (turnNotFinished())
            delay();

        game.getActionGuard().forbidActions();
        log.info("[TURN] FINISHED " + turnPhase);
    }

    private void initTurn() {
        drawCommunityCards();
        players.forEach(Player::resetTurn);
    }

    private void drawCommunityCards() {
        Integer nbCommunityCards = turnCommunityCardsMap.get(turnPhase);
        round.getCommunityCards().addAll(round.getDeck().drawCards(nbCommunityCards));
    }

    private Boolean turnNotFinished() {
        return !isFinished() && game.getActionManager().playActionIfExist(round);
    }

    public Boolean isFinished() {
        if (players.haveAllFoldExceptOne()) {
            return true;
        } else {
            return players.haveAllPlayed() && players.haveAllEqualBet();
        }
    }

    private void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(Game.DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Turn{" +
                "turnPhase=" + turnPhase +
                '}';
    }
}
