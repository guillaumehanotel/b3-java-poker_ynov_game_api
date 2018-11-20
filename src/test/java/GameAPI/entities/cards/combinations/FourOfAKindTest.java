package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;
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
