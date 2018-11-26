package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoyalFlushTest {

  @Test
  void RoyalFlush() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Ten),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ace)
    );
    assertEquals(new RoyalFlush(), new RoyalFlush(cards));
  }

  @Test
  void RoyalFlushFail() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.Ten),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ace)
    );
    assertThrows(CombinationNotPresentException.class, () -> new RoyalFlush(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 0, new RoyalFlush().comparesWithSame(new RoyalFlush()));
  }
}