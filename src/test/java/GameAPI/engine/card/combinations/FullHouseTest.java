package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FullHouseTest {

  @Test
  void buildBestFromCards() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    FullHouse expected = new FullHouse(Rank.Ace, Rank.Eight);
    Combination actual = new FullHouse(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildBestFromCardsFail() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    assertThrows(CombinationNotPresentException.class, () -> new FullHouse(sourceCards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) (-1), new FullHouse(Rank.Three, Rank.Queen).compares(new FullHouse(Rank.Three, Rank.King)));
  }

  @Test
  void comparesWithInferior() {
    assertEquals((Integer) 1, new FullHouse(Rank.Six, Rank.Nine).compares(new FullHouse(Rank.Five, Rank.Queen)));
  }
}
