package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuinteTest {

  @Test
  void Quinte() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.Queen),
        new Card(Suit.CLUB, Rank.Nine),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.SPADE, Rank.Ten)
    );
    assertEquals(new Quinte(Rank.King), new Quinte(cards));
  }

  @Test
  void QuinteDuplicate() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.Queen),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.CLUB, Rank.Nine),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.SPADE, Rank.Ten)
    );
    assertEquals(new Quinte(Rank.King), new Quinte(cards));
  }

  @Test
  void NoQuinte() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.DIAMOND, Rank.Queen),
        new Card(Suit.CLUB, Rank.Nine),
        new Card(Suit.CLUB, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.SPADE, Rank.Ace)
    );
    assertThrows(CombinationNotPresentException.class, () -> new Quinte(cards));
  }

  @Test
  void aceAsFirstCard() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.Ace),
        new Card(Suit.HEART, Rank.Two),
        new Card(Suit.HEART, Rank.Three),
        new Card(Suit.HEART, Rank.Four),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ten)
    );
    assertEquals(new Quinte(Rank.Five), new Quinte(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 1, new Quinte(Rank.Queen).comparesWithSame(new Quinte(Rank.Jack)));
  }

}