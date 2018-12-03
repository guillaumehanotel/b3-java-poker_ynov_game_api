package GameAPI.engine.user;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.combinations.*;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import GameAPI.engine.game.Game;
import GameAPI.engine.game.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Player {

    private User user;
    @JsonIgnore
    private Game game;

    private Integer chips;
    private Integer currentBet;
    private List<Role> roles;
    private Integer earnedMoney;

    private Boolean isEliminated;
    private Boolean hasDropped;
    private Boolean hasPlayedTurn;

    @JsonIgnore
    private List<Card> downCards;
    @JsonIgnore
    private List<Card> previousDownCards;
    @JsonIgnore
    private Combination _combination; //store combination without showing it in json
    private String combination;

    public Player(User user, Integer startingChips, Game game) {
        this.user = user;
        this.game = game;

        // lié à une partie
        this.isEliminated = false;
        this.chips = startingChips;

        // lié à un round
        this.currentBet = 0;
        this.roles = new ArrayList<>();
        this.hasDropped = false;
        this.downCards = new ArrayList<>();
        this.previousDownCards = new ArrayList<>();

        // lié à un tour
        this.hasPlayedTurn = false;
    }

    public void resetRound() {
        this.hasDropped = false;
        this.downCards.clear();
        this.currentBet = 0;
        this.roles.clear();
    }

    public void resetTurn() {
        this.hasPlayedTurn = false;
    }

    public void addCardToHand(Card card) {
        this.downCards.add(card);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void bets(Integer amount) {
        log.info(this.user.getUsername() + " bets " + amount);
        this.currentBet += amount;
        this.chips -= amount;
        game.setPot(game.getPot() + amount);
        this.hasPlayedTurn = true;
    }

    public void fold() {
        log.info(this.user.getUsername() + " fold");
        this.hasDropped = true;
        this.hasPlayedTurn = true;
    }

    public void call(Integer biggestBet) {
        log.info(this.user.getUsername() + " call");
        // si on a pas assez pour suivre, on mise tout ce qui nous reste
        if (biggestBet - currentBet < chips) {
            this.bets(biggestBet - currentBet);
        } else {
            this.bets(chips);
        }
        this.hasPlayedTurn = true;
    }

    private Boolean hasAllIn(){
        return currentBet.equals(chips + currentBet);
    }

    public boolean hasFold() {
        return hasDropped;
    }

    public boolean hasPlayedTurn() {
        return hasPlayedTurn;
    }

    @JsonIgnore
    public boolean isEliminated() { return isEliminated; }

    /**
     * Un joueur est considéré comme ignoré pour une manche si :
     * - il est éliminé
     * - il s'est couché
     * - il a misé tout ses jetons (all-in)
     */
    @JsonIgnore
    public Boolean isIgnoredForRound(){
        if (!isEliminated) {
            return hasAllIn() || hasDropped;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "player" + user.getUsername();
    }

    private Cards getAllCards() {
        Cards allCards = new Cards();
        allCards.addAll(downCards);
        allCards.addAll(game.getLastCommunityCards());
        return allCards;
    }

    public void win(Integer earnedMoney) {
        log.info(this.user.getUsername() + " WINS " + earnedMoney);
        this.chips += earnedMoney;
        this.combination = _combination != null ? _combination.toString() : null;
        log.info("Winning combination : " + _combination);
        this.earnedMoney = earnedMoney;
        user.setMoney(user.getMoney() + this.earnedMoney);
    }

    public void loose() {
        log.info(this.user.getUsername() + " LOOSES " + this.currentBet);
        this.combination = _combination != null ? _combination.toString() : null;
        log.info("Loosing combination : " + _combination);
        if(this.chips == 0){
            log.info(this + " is eliminated");
            this.isEliminated = true;
        }
    }

    public void savePreviousDownCards() {
        this.previousDownCards.clear();
        this.previousDownCards.addAll(downCards);
    }

    public Integer comparesCards(Player player) {
        Combination bestCombination = getBestCombination();
        Combination playerBestCombination = player.getBestCombination();
        if (bestCombination == null && playerBestCombination == null) return 0;
        else if (bestCombination == null) return -1;
        else if (playerBestCombination == null) return 1;
        return bestCombination.compares(playerBestCombination);
    }

    @JsonIgnore
    public Combination getBestCombination() {
        List<Combination> combinations = new ArrayList<>();
        Cards allCards = getAllCards();
        for (Class<? extends Combination> possibleCombination : game.getCombinationTypes()) {
            try {
                combinations.add(possibleCombination.getDeclaredConstructor(Cards.class).newInstance(allCards));
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                if (e.getCause().getClass() != CombinationNotPresentException.class) e.printStackTrace();
            }
        }
        Combination bestCombination = combinations.stream().max(Combination::compares).orElse(null);
        this._combination = bestCombination;
        return bestCombination;
    }

}
