package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Card;
import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.Suit;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaireTest {

  @Test
  void Paire() {
    Cards cards = new Cards(
        new Card(Suit.CLUB, Rank.Two),
        new Card(Suit.HEART, Rank.Two)
    );
    Paire expected = new Paire(Rank.Two);
    Paire actual = new Paire(cards);
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
    Paire expected = new Paire(Rank.Ace);
    Paire actual = new Paire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards2() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Eight)
    );
    assertThrows(CombinationNotPresentException.class, () -> new Paire(sourceCards));
  }

  @Test
  void buildFromCards3() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.CLUB, Rank.Ace)
    );
    Paire expected = new Paire(Rank.Ace);
    Paire actual = new Paire(sourceCards);
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
    Paire expected = new Paire(Rank.Ace);
    Paire actual = new Paire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void buildFromCards5() {
    Cards sourceCards = new Cards(
        new Card(Suit.CLUB, Rank.Ace),
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.SPADE, Rank.Ace)
    );
    Paire expected = new Paire(Rank.Ace);
    Paire actual = new Paire(sourceCards);
    assertEquals(expected, actual);
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 0, new Paire(Rank.Ace).comparesWithSame(new Paire(Rank.Ace)));
  }

  @Test
  void comparesWithOtherSuperior() {
    Paire paire = new Paire(Rank.Ace);
    DoublePaire doublePaire = new DoublePaire(Rank.Five, Rank.Four);
    assertEquals((Integer) (-1), paire.compares(doublePaire));
  }
}
