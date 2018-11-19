package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrelanTest {

  @Test
  void Brelan() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Seven),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.King)
    );
    assertEquals(new Brelan(Rank.Eight), new Brelan(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) (-1), new Brelan(Rank.King).compares(new Brelan(Rank.Ace)));
  }
}
