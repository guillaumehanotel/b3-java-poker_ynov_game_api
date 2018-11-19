package GameAPI.entities;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.combinations.*;
import GameAPI.entities.cards.combinations.exceptions.CombinationCreationError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Player {

    private User user;
    private Game game;
    private Integer chips;
    private Boolean isEliminated;
    private Boolean hasDropped;
    private Integer currentBet;
    private Boolean hasPlayTurn;
    private List<Card> downCards;
    private Combination combination;
    private Integer earnedMoney;
    @JsonIgnore
    private Combination _combination; //store combination without showing it in json

    public Player(User user, Integer startingChips, Game game) {
        this.user = user;
        this.game = game;

        // lié à une partie
        this.isEliminated = false;
        this.chips = startingChips;

        // lié à un round
        this.hasDropped = false;
        this.downCards = new ArrayList<>();
        this.currentBet = 0;

        // lié à un tour
        this.hasPlayTurn = false;
    }

    public void resetRound(){
        this.hasDropped = false;
        this.downCards.clear();
        this.currentBet = 0;
    }

    public void resetTurn(){
        this.hasPlayTurn = false;
    }

    public void addCardToHand(Card card) {
        this.downCards.add(card);
    }

    public void bets(Integer amount) {
        log.info(this.user.getUsername() + " bets " + amount);
        this.currentBet = this.currentBet + amount;
        this.hasPlayTurn = true;
    }

    public void fold() {
        log.info(this.user.getUsername() + " fold");
        this.hasDropped = true;
        this.hasPlayTurn = true;
    }

    public void call(Integer biggestBet) {
        log.info(this.user.getUsername() + " call");
        this.bets(biggestBet - currentBet);
        this.hasPlayTurn = true;
    }

    public void raise(Integer amount) {
        log.info(this.user.getUsername() + " raise " + amount);
        bets(amount);
        this.hasPlayTurn = true;

    }
    // TODO : renvoyer les minimum raise amount pour l'interface

    public void check() {
        log.info(this.user.getUsername() + " check");
        this.hasPlayTurn = true;
    }

    public void allIn() {
        log.info(this.user.getUsername() + " allin");
        this.hasPlayTurn = true;
        bets(chips - currentBet);
    }

    @Override
    public String toString() {
        return String.valueOf(user) + "\n";
    }

    public Integer comparesCards(Player player) {
        Combination bestCombination = getBestCombination();
        Combination playerBestCombination = player.getBestCombination();
        if (bestCombination == null && playerBestCombination == null) return 0;
        else if (bestCombination == null) return -1;
        else if (playerBestCombination == null) return 1;
        return bestCombination.compares(playerBestCombination);
    }

    public Combination getBestCombination() {
        List<Combination> combinations = new ArrayList<Combination>() {
            @Override
            public boolean add(Combination o) {
                if (o == null) return false;
                return super.add(o);
            }
        };
        Cards allCards = getAllCards();
        try {
            combinations.add(new Hauteur(allCards));
        } catch (CombinationCreationError ignored) {
        }
        try {
            combinations.add(new Paire(allCards));
        } catch (CombinationCreationError ignored) {
        }
        try {
            combinations.add(new Full(allCards));
        } catch (CombinationCreationError ignored) {
        }
        try {
            combinations.add(new DoublePaire(allCards));
        } catch (CombinationCreationError ignored) {
        }
        Combination bestCombination = combinations.stream().max(Combination::compares).orElse(null);
        System.out.println(toString() + " : " + bestCombination);
        this._combination = bestCombination;
        return bestCombination;
    }

    private Cards getAllCards() {
        Cards allCards = new Cards();
        allCards.addAll(downCards);
        allCards.addAll(game.getLastCommunityCards());
        return allCards;
    }

    public void win(Integer earnedMoney) {
        this.chips += earnedMoney;
        this.combination = _combination;
        this.earnedMoney = earnedMoney;
    }

    void loose() {
        this.chips -= this.currentBet;
    }
}
