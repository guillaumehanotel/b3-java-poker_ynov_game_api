package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreeOfAKindTest {

  @Test
  void ThreeOfAKind() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Seven),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.King)
    );
    assertEquals(new ThreeOfAKind(Rank.Eight), new ThreeOfAKind(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) (-1), new ThreeOfAKind(Rank.King).compares(new ThreeOfAKind(Rank.Ace)));
  }
}
