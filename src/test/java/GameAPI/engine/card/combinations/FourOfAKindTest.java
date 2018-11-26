package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FourOfAKindTest {

  @Test
  void FourOfAKind() {
    FourOfAKind expected = new FourOfAKind(Rank.Ace, Rank.King);
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    FourOfAKind actual = new FourOfAKind(cards);
    assertEquals(expected, actual);
  }

  @Test
  void FourOfAKindFail() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.HEART, Rank.Four),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    assertThrows(CombinationNotPresentException.class, () -> new FourOfAKind(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals(
        (Integer) (-1),
        new FourOfAKind(Rank.Jack, Rank.Queen).comparesWithSame(new FourOfAKind(Rank.Jack, Rank.King))
    );
  }

}
