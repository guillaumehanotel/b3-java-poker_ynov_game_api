package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Card;
import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.Suit;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouleurTest {

  @Test
  void Couleur() {
    Couleur expected = new Couleur(Suit.HEART, Rank.Queen);
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    Couleur actual = new Couleur(cards);
    assertEquals(expected, actual);
  }

  @Test
  void CouleurFail() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.SPADE, Rank.Queen),
        new Card(Suit.DIAMOND, Rank.Queen)
    );
    assertThrows(CombinationNotPresentException.class, () -> new Couleur(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 1, new Couleur(Suit.HEART, Rank.Queen).comparesWithSame(new Couleur(Suit.SPADE, Rank.Jack)));
  }
}
