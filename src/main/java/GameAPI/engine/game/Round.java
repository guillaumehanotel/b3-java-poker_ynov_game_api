package GameAPI.engine.game;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Deck;
import GameAPI.engine.user.Player;
import GameAPI.engine.user.PlayerStatus;
import GameAPI.engine.user.Players;
import GameAPI.services.StatsService;
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

    StatsService statsService;

    @JsonIgnore
    private Game game;
    @JsonIgnore
    private Deck deck;
    @JsonIgnore
    private Players players;
    private List<Card> communityCards;
    private TurnPhase currentTurnPhase;
    private List<Integer> winnerIds = new ArrayList<>();

    Round(Game game) {
        this.statsService = new StatsService();
        this.game = game;
        this.deck = new Deck();
        this.players = new Players();
        this.players.addAll(game.getNonEliminatedPlayers());
        this.communityCards = new ArrayList<>();
        this.currentTurnPhase = TurnPhase.PREFLOP;
    }

    void start() {
        log.info("[ROUND] START");
        game.addFlag(GameFlag.NEW_ROUND);
        init();
        playTurns();
        showDown();
        players.forEach(Player::syncMoneyWithChips);
        log.info("[ROUND] FINISHED");
    }

    /**
     * Mise des blinds
     * Distribution des cartes
     */
    private void init() {
        players.forEach(Player::savePreviousDownCards);
        players.forEach(Player::resetRound);
        applyBlindsBet();
        handOutCards();
    }

    private void applyBlindsBet() {
        players.setCurrentOrderIndex(game.getDealerPosition() + 1);
        players.getNextPlaying().bets(game.getSmallBlind());
        players.getNextPlaying().bets(game.getBigBlind());
    }

    private void handOutCards() {
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                player.addCardToHand(deck.pop());
            }
        }
    }

    private void playTurns() {
        int i = 0;
        int nbPhase = TurnPhase.values().length;

        startTurn();
        while (i != nbPhase - 1) {
            currentTurnPhase = currentTurnPhase.getNextPhase();
            startTurn();
            i++;
        }
    }

    /**
     * Lancement d'un tour de mise en admettant que les cartes aient été distribuées et que les blinds aient été misées
     * Un tour de mise continue tant que :
     * - tout le monde n'a pas joué 1 fois
     * - tout le monde (pas couché) n'a pas la même mise
     */
    private void startTurn() {
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

        // Dans le cas où tous sauf 1 se couche
        if (players.haveAllFinishedTurn()) game.markActionAsProcessed();

        while (turnNotFinished()) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        game.getActionGuard().forbidActions();
        log.info("[TURN] FINISHED");
    }

    private void initTurn() {
        setupTurnByPhase();
        players.forEach(Player::resetTurn);
    }

    private void setupTurnByPhase() {
        TurnBehavior turnBehavior = TurnBehavior.getInstance();
        Method turnInitMethod = turnBehavior.getInitMethodByTurnPhase(currentTurnPhase);
        try {
            turnInitMethod.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void setupPreFlop() {
        log.info("[TURN] PREFLOP");
        players.setCurrentOrderIndex((game.getDealerPosition() + 3) % Game.NB_PLAYER_MAX);
    }

    @SuppressWarnings("WeakerAccess")
    public void setupFlop() {
        log.info("[TURN] FLOP");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(3));
    }

    @SuppressWarnings("WeakerAccess")
    public void setupTurn() {
        log.info("[TURN] TURN");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(1));
    }

    @SuppressWarnings("WeakerAccess")
    public void setupRiver() {
        log.info("[TURN] RIVER");
        players.setCurrentOrderIndex((game.getDealerPosition() + 1) % Game.NB_PLAYER_MAX);
        communityCards.addAll(deck.drawCards(1));
    }

    @SuppressWarnings("WeakerAccess")
    public void showDown() {
        log.info("[TURN] SHOWDOWN");
        game.addFlag(GameFlag.SHOWDOWN);
        HashMap<PlayerStatus, List<Player>> playersByResult = players.getPlayersByResult();
        creditWinners(playersByResult.get(PlayerStatus.WINNER));
        debitLoosers(playersByResult.get(PlayerStatus.LOOSER));
    }

    private void creditWinners(List<Player> winners) {
        Integer pot = getPot();
        Integer earnedMoneyByPlayer = pot / winners.size();
        for (Player player : winners) {
            player.win(earnedMoneyByPlayer);
            //insertUserResult(player, earnedMoneyByPlayer);
            winnerIds.add(player.getUser().getId());
        }
    }

    private void debitLoosers(List<Player> loosers) {
        for (Player looser : loosers) {
            looser.loose();
            //insertUserResult(looser, looser.getCurrentBet());
        }
    }

    private void insertUserResult(Player player, Integer moneyWon) {
        statsService.createResults(
                player.getUser().getId(),
                game.getId(),
                moneyWon,
                new Date(),
                player.getCombination()
        );
    }

    private Integer getPot() {
        return players.stream().mapToInt(Player::getCurrentBet).sum();
    }

    private Boolean turnNotFinished() {
        return players.haveAllFinishedTurn() && game.getActionManager().playActionIfExist(this);
    }

    public Integer getBiggestBet() {
        Optional<Player> player = players.stream().max(Comparator.comparingInt(Player::getCurrentBet));
        return player.isPresent() ? player.get().getCurrentBet() : Integer.valueOf(0);
    }

}
