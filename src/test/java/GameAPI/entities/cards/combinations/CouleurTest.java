package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;
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
