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

import java.util.*;

@Data
@Slf4j
public class Round {

    @JsonIgnore
    StatsService statsService;
    @JsonIgnore
    private Game game;
    @JsonIgnore
    private Deck deck;
    @JsonIgnore
    private Players players;
    private List<Card> communityCards;
    @JsonIgnore
    private Turn currentTurn;
    private List<Integer> winnerIds = new ArrayList<>();

    Round(Game game) {
        this.statsService = new StatsService();
        this.game = game;
        this.deck = new Deck();
        this.players = new Players(game.getPlayers(), game.getDealerPosition());
        this.communityCards = new ArrayList<>();
        this.currentTurn = null;
    }

    void start() {
        log.info("[ROUND] START");
        game.addFlag(GameFlag.NEW_ROUND);
        initRound();
        playTurns();
        showDown();
        log.info("[ROUND] FINISHED");
    }

    /**
     * Sauvegarde des cartes de la manche passé pour chaque user
     * Réinitialisation des propriétés d'une manche pour chaque user
     * Mise des blinds
     * Distribution des cartes
     */
    private void initRound() {
        players.forEach(Player::savePreviousDownCards);
        players.forEach(Player::resetRound);
        players.attributeDealer();
        applyBlindsBet();
        handOutCards();
    }

    private void applyBlindsBet() {
        players.getNextPlayingPlayer().bets(game.getSmallBlind());
        players.getPlayingPlayer().addRole(Role.SMALL_BLIND);
        players.getNextPlayingPlayer().bets(game.getBigBlind());
        players.getPlayingPlayer().addRole(Role.BIG_BLIND);
    }

    private void handOutCards() {
        for (int i = 0; i < 2; i++)
            for (Player player : players)
                player.addCardToHand(deck.pop());
    }

    private void playTurns() {
        for (TurnPhase turnPhase : TurnPhase.values()) {
            currentTurn = new Turn(this, turnPhase);
            currentTurn.play();
            players.initPlayingPlayerPosition();
        }
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
            insertUserResult(player, earnedMoneyByPlayer);
            winnerIds.add(player.getUser().getId());
        }
    }

    private Integer getPot() {
        return players.stream().mapToInt(Player::getCurrentBet).sum();
    }

    private void debitLoosers(List<Player> loosers) {
        log.info(loosers.toString());
        for (Player looser : loosers) {
            looser.loose();
            insertUserResult(looser, -looser.getCurrentBet());
        }
    }

    private void insertUserResult(Player player, Integer moneyWon) {
        statsService.createResults(
                player.getUser().getId(),
                game.getId(),
                moneyWon,
                player.getCombination()
        );
    }

    public void passToNextPlayerAndWaitAction(){
        game.getActionGuard().expectActionFrom(players.getNextPlayingPlayer());
        game.updatePlayingPlayerData(players.getPlayingPlayer(), getBiggestBet());
    }

    public Integer getBiggestBet() {
        Optional<Player> player = players.stream().max(Comparator.comparingInt(Player::getCurrentBet));
        return player.isPresent() ? player.get().getCurrentBet() : Integer.valueOf(0);
    }

}
