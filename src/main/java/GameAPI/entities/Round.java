package GameAPI.entities;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Deck;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Round {

    @JsonIgnore
    private Game game;
    @JsonIgnore
    private Deck deck;
    @JsonIgnore
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
    }

    public void start() {
        log.info("[ROUND] START");
        game.addFlag(GameFlag.NEW_ROUND);
        initRound();
        playTurns();
        showDown();
        log.info("[ROUND] FINISHED");
    }

    /**
     * Mise des blinds
     * Distribution des cartes
     */
    private void initRound() {
        players.forEach(Player::resetRound);
        applyBlindsBet();
        handOutCards();
    }

    private void applyBlindsBet() {
        this.players.setCurrentOrderIndex(this.game.getDealerPosition() + 1);
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

    private void playTurns() {
        int i = 0;
        int nbPhase = TurnPhase.values().length;

        this.startTurn();
        while (i != nbPhase - 1) {
            this.currentTurnPhase = this.currentTurnPhase.getNextPhase();
            this.startTurn();
            i++;
        }
    }

    /**
     * Lancement d'un tour de mise en admettant que les cartes aient été distribuées et que les blinds ont été misées
     * Un tour de mise continue tant que :
     * - tout le monde ait joué 1 fois
     * - tout le monde (pas couché) aient la même mise
     */
    public void startTurn() {
        log.info("[TURN] START");
        game.addFlag(GameFlag.NEW_TURN);
        initTurn();
        game.getActionGuard().expectActionFrom(players.getPlayingPlayer());
        game.updatePlayingPlayerData(this);
        try {
            game.joinQueue.put(game);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (turnNotFinishCondition()) game.save();

        while (turnNotFinish()) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        game.getActionGuard().forbidActions();
        log.info("[TURN] FINISHED");
    }

    public void initTurn() {
        setupTurnByPhase();
        players.forEach(Player::resetTurn);
    }

    public void setupTurnByPhase() {
        TurnBehavior turnBehavior = TurnBehavior.getInstance();
        Method turnInitMethod = turnBehavior.getInitMethodByTurnPhase(currentTurnPhase);
        try {
            turnInitMethod.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupPreFlop() {
        log.info("[TURN] PREFLOP");
        players.setCurrentOrderIndex((game.getDealerPosition() + 3) % Game.NB_PLAYER_MAX);
    }

    public void setupFlop() {
        log.info("[TURN] FLOP");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(3));
    }

    public void setupTurn() {
        log.info("[TURN] TURN");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(1));
    }

    public void setupRiver() {
        log.info("[TURN] RIVER");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(1));
    }

    private void showDown() {
        log.info("[TURN] SHOWDOWN");
        game.addFlag(GameFlag.SHOWDOWN);
        HashMap<PlayerStatus, List<Player>> playersByResult = players.getPlayersByResult();
        Integer pot = players.stream().mapToInt(Player::getCurrentBet).sum();
        List<Player> winners = playersByResult.get(PlayerStatus.WINNER);
        Integer earnedMoneyByPlayer = pot / winners.size();
        winners.forEach(player -> player.win(earnedMoneyByPlayer));
        playersByResult.get(PlayerStatus.LOOSER).forEach(Player::loose);
    }

    private Boolean turnNotFinish() {
        return turnNotFinishCondition() && game.getActionManager().playActionIfExist(this);
    }

    /**
     * Le tour n'est pas fini tant que les 2 conditions ne sont pas remplis
     */
    public Boolean turnNotFinishCondition() {
        if (isThereOnePlayingPlayerInRound()) {
            return false;
        } else {
            return (!haveAllPlayersPlayed() || !haveAllPlayersEqualBet());
        }
    }

    /**
     * Est-ce que tous les joueurs qui ne se sont pas couchés ont joué ?
     */
    private Boolean haveAllPlayersPlayed() {
        List<Player> playersStillPlaying = players.stream()
                .filter(player -> !player.isIgnoredForRound())
                .collect(Collectors.toList());
        // todo replace by stream method
        for (Player player : playersStillPlaying) {
            if (!player.getHasPlayTurn()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Est-ce que tous les joueurs qui ne se sont pas couchés ont des mises égales ?
     */
    private Boolean haveAllPlayersEqualBet() {
        return players.stream()
                .filter(player -> !player.isIgnoredForRound())
                .map(Player::getCurrentBet)
                .allMatch(bet -> bet.equals(players.get(0).getCurrentBet()));
    }

    /**
     * Vérifie si il ne reste qu'un joueur restant dans une manche
     */
    private Boolean isThereOnePlayingPlayerInRound() {
        // on compte tous les joueurs s'étant couché, si NB = NB_JOUEUR - 1
        return this.players.stream()
                .filter(Player::getHasDropped)
                .count() == Game.NB_PLAYER_MAX - 1;
    }

    Integer getBiggestBet() {
        Optional<Player> player = players.stream().max(Comparator.comparingInt(Player::getCurrentBet));
        return player.isPresent() ? player.get().getCurrentBet() : Integer.valueOf(0);
    }

}
