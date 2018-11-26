package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublePairTest {

  @Test
  void DoublePair() {
    List<Rank> expected = Arrays.asList(Rank.Ten, Rank.Eight);
    List<Rank> actual = new DoublePair(Rank.Eight, Rank.Ten).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePair1() {
    List<Rank> expected = Arrays.asList(Rank.Eight, Rank.Two);
    List<Rank> actual = new DoublePair(Rank.Two, Rank.Eight).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePair2() {
    List<Rank> expected = Arrays.asList(Rank.Ace, Rank.Two);
    List<Rank> actual = new DoublePair(Rank.Two, Rank.Ace).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePairBuilder() {
    DoublePair expected = new DoublePair(Rank.Jack, Rank.Eight);
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Jack),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    DoublePair actual = new DoublePair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void DoublePairBuilderMultiple() {
    DoublePair expected = new DoublePair(Rank.Jack, Rank.Queen);
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Jack),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen),
        new Card(Suit.CLUB, Rank.Ten),
        new Card(Suit.DIAMOND, Rank.Ten),
        new Card(Suit.SPADE, Rank.Queen)
    );
    DoublePair actual = new DoublePair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void DoublePairBuilderFail() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.SPADE, Rank.Jack),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    assertThrows(CombinationNotPresentException.class, () -> new DoublePair(sourceCards));
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
    DoublePair expected = new DoublePair(Rank.Eight, Rank.Ace);
    DoublePair actual = new DoublePair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards2() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.DIAMOND, Rank.Ace),
        new Card(Suit.CLUB, Rank.Ace)
    );
    DoublePair expected = new DoublePair(Rank.Ace, Rank.Ace);
    DoublePair actual = new DoublePair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards3() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.DIAMOND, Rank.Ace)
    );
    DoublePair expected = new DoublePair(Rank.Eight, Rank.Ace);
    DoublePair actual = new DoublePair(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSameEquals() {
    DoublePair first = new DoublePair(Rank.Ace, Rank.Jack);
    DoublePair second = new DoublePair(Rank.Jack, Rank.Ace);
    assertEquals((Integer) 0, first.compares(second));
  }

  @Test
  void comparesWithSameSuperior() {
    DoublePair first = new DoublePair(Rank.Ace, Rank.Jack);
    DoublePair second = new DoublePair(Rank.Ace, Rank.Two);
    assertEquals((Integer) 1, first.compares(second));
  }

  @Test
  void comparesWithSameInferior() {
    DoublePair first = new DoublePair(Rank.King, Rank.Jack);
    DoublePair second = new DoublePair(Rank.Ace, Rank.Two);
    assertEquals((Integer) (-1), first.compares(second));
  }

  @Test
  void comparesWithOtherInferior() {
    Pair pair = new Pair(Rank.Five);
    DoublePair doublePair = new DoublePair(Rank.Three, Rank.Two);
    assertEquals((Integer) 1, doublePair.compares(pair));
  }
}
