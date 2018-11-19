package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import GameAPI.entities.cards.combinations.exceptions.CombinationCreationError;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublePaireTest {

  @Test
  void DoublePaire() {
    List<Rank> expected = Arrays.asList(Rank.Ten, Rank.Eight);
    List<Rank> actual = new DoublePaire(Rank.Eight, Rank.Ten).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePaire1() {
    List<Rank> expected = Arrays.asList(Rank.Eight, Rank.Two);
    List<Rank> actual = new DoublePaire(Rank.Two, Rank.Eight).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePaire2() {
    List<Rank> expected = Arrays.asList(Rank.Ace, Rank.Two);
    List<Rank> actual = new DoublePaire(Rank.Two, Rank.Ace).getRanksForTest();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void DoublePaireBuilder() {
    DoublePaire expected = new DoublePaire(Rank.Jack, Rank.Eight);
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Jack),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    DoublePaire actual = new DoublePaire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void DoublePaireBuilderMultiple() {
    DoublePaire expected = new DoublePaire(Rank.Jack, Rank.Queen);
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
    DoublePaire actual = new DoublePaire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void DoublePaireBuilderFail() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.SPADE, Rank.Jack),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.CLUB, Rank.Queen)
    );
    assertThrows(CombinationCreationError.class, () -> new DoublePaire(sourceCards));
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
    DoublePaire expected = new DoublePaire(Rank.Eight, Rank.Ace);
    DoublePaire actual = new DoublePaire(sourceCards);
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
    DoublePaire expected = new DoublePaire(Rank.Ace, Rank.Ace);
    DoublePaire actual = new DoublePaire(sourceCards);
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
    DoublePaire expected = new DoublePaire(Rank.Eight, Rank.Ace);
    DoublePaire actual = new DoublePaire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSameEquals() {
    DoublePaire first = new DoublePaire(Rank.Ace, Rank.Jack);
    DoublePaire second = new DoublePaire(Rank.Jack, Rank.Ace);
    assertEquals((Integer) 0, first.compares(second));
  }

  @Test
  void comparesWithSameSuperior() {
    DoublePaire first = new DoublePaire(Rank.Ace, Rank.Jack);
    DoublePaire second = new DoublePaire(Rank.Ace, Rank.Two);
    assertEquals((Integer) 1, first.compares(second));
  }

  @Test
  void comparesWithSameInferior() {
    DoublePaire first = new DoublePaire(Rank.King, Rank.Jack);
    DoublePaire second = new DoublePaire(Rank.Ace, Rank.Two);
    assertEquals((Integer) (-1), first.compares(second));
  }

  @Test
  void comparesWithOtherInferior() {
    Paire paire = new Paire(Rank.Five);
    DoublePaire doublePaire = new DoublePaire(Rank.Three, Rank.Two);
    assertEquals((Integer) 1, doublePaire.compares(paire));
  }
}
