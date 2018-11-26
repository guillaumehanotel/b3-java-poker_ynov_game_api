package GameAPI.engine.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck extends Stack<Card> {

    public Deck() {
        generateDeck();
        shuffleDeck();
    }

    private void generateDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                this.push(new Card(suit, rank));
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(this);
        Collections.shuffle(this);
    }

    public List<Card> drawCards(Integer nbr) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < nbr; i++) {
            cards.add(this.pop());
        }
        return cards;
    }

    public void printDeck() {
        this.forEach(System.out::println);
    }
}

