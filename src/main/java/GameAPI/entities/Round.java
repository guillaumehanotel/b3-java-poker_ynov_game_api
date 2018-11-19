package GameAPI.entities;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Deck;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Round {

    private Game game;
    private Deck deck;
    private Players players;
    private List<Card> communityCards;
    private TurnPhase currentTurnPhase;

    public Round(Game game) {
        this.game = game;
        this.deck = new Deck();
        this.players = new Players();
        this.players.addAll(game.getNonEliminatedPlayers());
        this.communityCards = new ArrayList<>();
        this.currentTurnPhase = TurnPhase.PREFLOP;
        this.start();
    }


    public void start() {
        log.info("START ROUND");
        initRound();
        int i = 0;
        int nbPhase = TurnPhase.values().length;
        while (i != nbPhase) {
            this.startTurn();
            this.currentTurnPhase = this.currentTurnPhase.getNextPhase();
            i++;
        }
        showDown();
    }

    /**
     * Incrémentation du dealer
     * Mise des blinds
     * Distribution des cartes
     */
    private void initRound() {
        players.forEach(Player::resetRound);
        if (game.getRounds().size() != 0) {
            game.incrementDealerPosition();
        }
        applyBlindsBet();
        handOutCards();
    }

    private void applyBlindsBet() {
        this.players.setCurrentOrderIndex(this.game.getDealerPosition());
        this.players.getNextPlaying().bets(game.getSmallBlind());
        this.players.getNextPlaying().bets(game.getBigBlind());
    }

    private void handOutCards() {
        for (int i = 0; i < 2; i++) {
            for (Player player : this.players) {
                player.addCardToHand(deck.pop());
            }
        }
    }

    /**
     * Lancement d'un tour de mise en admettant que les cartes aient été distribuées et que les blinds ont été misées
     * Un tour de mise continue tant que :
     * - tout le monde ait joué 1 fois
     * - tout le monde (pas couché) aient la même mise
     */
    public void startTurn() {
        log.info("Start TURN");
        initTurn();
        game.getActionGuard().expectActionFrom(players.getPlayingPlayer());

        log.info("waiting for an action from player [" + players.getPlayingPlayer().getUser().getUsername() + "]");
        while (turnNotFinish()) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("Turn is finished");
        game.getActionGuard().forbidActions();
    }

    public void initTurn() {
        TurnBehavior turnBehavior = TurnBehavior.getInstance();
        Method turnInitMethod = turnBehavior.getInitMethodByTurnPhase(currentTurnPhase);
        try {
            turnInitMethod.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        players.forEach(Player::resetTurn);
    }

    public void setupPreFlop() {
        log.info("== PREFLOP ==");
        players.setCurrentOrderIndex(game.getDealerPosition() + 3);
    }

    public void setupFlop() {
        log.info("== FLOP ==");
        players.setCurrentOrderIndex(game.getDealerPosition() + 1);
        communityCards.addAll(deck.drawCards(3));
    }

    public void setupTurn() {
        log.info("== TURN ==");
        players.setCurrentOrderIndex(game.getDealerPosition() + 1);
        communityCards.addAll(deck.drawCards(1));
    }

    public void setupRiver() {
        log.info("== RIVER ==");
        players.setCurrentOrderIndex(game.getDealerPosition() + 1);
        communityCards.addAll(deck.drawCards(1));
    }

    private void showDown() {
        log.info("== SHOWDOWN ==");


    }

    /**
     * Le tour n'est pas fini tant que les 2 conditions ne sont pas remplis
     */
    private boolean turnNotFinish() {
        //log.info(haveAllPlayersPlayed() ? "tous les joueurs ont joué" : "tous les joueurs n'ont pas joués");
        //log.info(haveAllPlayersEqualBet() ? "tous les joueurs ont la meme mise" : "tous les joueurs n'ont pas la meme mise");

        return (!haveAllPlayersPlayed() || !haveAllPlayersEqualBet()) && game.getActionManager().checkPlayerAction(this);
    }

    /**
     * Est-ce que tous les joueurs qui ne se sont pas couchés ont joué ?
     */
    private boolean haveAllPlayersPlayed() {
        List<Player> playersStillPlaying =  players.stream()
                .filter(playerRound -> !playerRound.getHasDropped())
                .collect(Collectors.toList());
        for (Player player : playersStillPlaying){
            if (!player.getHasPlayTurn()){
                return false;
            }
        }
        return true;
    }

    /**
     * Est-ce que tous les joueurs qui ne se sont pas couchés ont des mises égales ?
     */
    private boolean haveAllPlayersEqualBet() {
        return players.stream()
                .filter(player -> !player.getHasDropped())
                .map(Player::getCurrentBet)
                .allMatch(bet -> bet.equals(players.get(0).getCurrentBet()));
    }

    Integer getBiggestBet() {
        Optional<Player> player = players.stream().max(Comparator.comparingInt(Player::getCurrentBet));
        return player.isPresent() ? player.get().getCurrentBet() : Integer.valueOf(0);
    }

}
