package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlushTest {

  @Test
  void Flush() {
    Flush expected = new Flush(Rank.Queen);
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    Flush actual = new Flush(cards);
    assertEquals(expected, actual);
  }

  @Test
  void FlushFail() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    assertThrows(CombinationNotPresentException.class, () -> new Flush(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 1, new Flush(Rank.Queen).comparesWithSame(new Flush(Rank.Jack)));
  }
}
