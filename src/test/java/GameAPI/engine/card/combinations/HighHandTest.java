package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

class HighHandTest {

  @Test
  void constructor() {
    HighHand highHand = new HighHand(Arrays.asList(Rank.King, Rank.Two, Rank.Two, Rank.Ten));
    List<Rank> actual = Arrays.asList(Rank.King, Rank.Ten, Rank.Two, Rank.Two);
    assertArrayEquals(highHand.getRanksForTest().toArray(), actual.toArray());
  }

  @Test
  void buildFromCards() {
    HighHand expected = new HighHand(Arrays.asList(Rank.Ace, Rank.Ace, Rank.Ace, Rank.King, Rank.Jack, Rank.Eight, Rank.Eight));
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ace),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.DIAMOND, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.King),
        new Card(Suit.DIAMOND, Rank.Ace));
    HighHand actual = new HighHand(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSameInferior() {
    HighHand first = new HighHand(Arrays.asList(Rank.Ace, Rank.King, Rank.Two));
    HighHand second = new HighHand(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    assertEquals((Integer) (-1), first.comparesWithSame(second));
  }

  @Test
  void comparesWithSameEquals() {
    HighHand first = new HighHand(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    HighHand second = new HighHand(Arrays.asList(Rank.Ace, Rank.King, Rank.Three));
    assertEquals((Integer) 0, first.comparesWithSame(second));
  }

}
