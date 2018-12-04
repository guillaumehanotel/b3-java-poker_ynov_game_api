package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Card;
import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.Suit;
import GameAPI.Game.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PairTest {

  @Test
  void Pair() {
    Cards cards = new Cards(
        new Card(Suit.CLUB, Rank.Two),
        new Card(Suit.HEART, Rank.Two)
    );
    Pair expected = new Pair(Rank.Two);
    Pair actual = new Pair(cards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards1() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    Pair expected = new Pair(Rank.Ace);
    Pair actual = new Pair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards2() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight)
    );
    assertThrows(CombinationNotPresentException.class, () -> new Pair(sourceCards));
  }

  @Test
  void buildFromCards3() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Ace)
    );
    Pair expected = new Pair(Rank.Ace);
    Pair actual = new Pair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards4() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.DIAMOND, Rank.Ace)
    );
    Pair expected = new Pair(Rank.Ace);
    Pair actual = new Pair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards5() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.SPADE, Rank.Ace)
    );
    Pair expected = new Pair(Rank.Ace);
    Pair actual = new Pair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 0, new Pair(Rank.Ace).comparesWithSame(new Pair(Rank.Ace)));
  }

  @Test
  void comparesWithOtherSuperior() {
    Pair pair = new Pair(Rank.Ace);
    DoublePair doublePair = new DoublePair(Rank.Five, Rank.Four);
    assertEquals((Integer) (-1), pair.compares(doublePair));
  }
}
