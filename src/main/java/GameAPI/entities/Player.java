package GameAPI.entities;

import GameAPI.entities.cards.Card;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Player {

    private User user;
    private Integer chips;
    private Boolean isEliminated;
    private Boolean hasDropped;
    private Integer currentBet;
    private Boolean hasPlayTurn;
    private List<Card> downCards;

    public Player(User user, Integer startingChips) {
        this.user = user;

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
}
