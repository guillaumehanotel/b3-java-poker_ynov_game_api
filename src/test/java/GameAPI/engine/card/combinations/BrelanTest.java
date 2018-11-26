package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
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
