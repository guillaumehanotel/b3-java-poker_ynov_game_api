package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

class HauteurTest {

  @Test
  void constructor() {
    Hauteur hauteur = new Hauteur(Arrays.asList(Rank.King, Rank.Two, Rank.Two, Rank.Ten));
    List<Rank> actual = Arrays.asList(Rank.King, Rank.Ten, Rank.Two, Rank.Two);
    assertArrayEquals(hauteur.getRanksForTest().toArray(), actual.toArray());
  }

  @Test
  void buildFromCards() {
    Hauteur expected = new Hauteur(Arrays.asList(Rank.Ace, Rank.Ace, Rank.Ace, Rank.King, Rank.Jack, Rank.Eight, Rank.Eight));
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.DIAMOND, Rank.Ace));
    Hauteur actual = new Hauteur(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSameInferior() {
    Hauteur first = new Hauteur(Arrays.asList(Rank.Ace, Rank.King, Rank.Two));
    Hauteur second = new Hauteur(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    assertEquals((Integer) (-1), first.comparesWithSame(second));
  }

  @Test
  void comparesWithSameEquals() {
    Hauteur first = new Hauteur(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    Hauteur second = new Hauteur(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    assertEquals((Integer) 0, first.comparesWithSame(second));
  }

}
