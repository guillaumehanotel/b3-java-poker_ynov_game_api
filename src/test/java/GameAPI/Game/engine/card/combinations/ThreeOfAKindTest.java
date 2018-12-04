package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Card;
import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.Suit;
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
