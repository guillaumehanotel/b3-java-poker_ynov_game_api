package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Card;
import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.Suit;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StraightFlushTest {

  @Test
  void StraightFlush() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ten)
    );
    assertEquals(new StraightFlush(Rank.King), new StraightFlush(cards));
  }

  @Test
  void StraightFlush1() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ten)
    );
    assertEquals(new StraightFlush(Rank.Queen), new StraightFlush(cards));
  }

  @Test
  void StraightFlushFail() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Ten),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ten)
    );
    assertThrows(CombinationNotPresentException.class, () -> new StraightFlush(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) (-1), new StraightFlush(Rank.Eight).comparesWithSame(new StraightFlush(Rank.Ten)));
  }
}
