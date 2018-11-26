package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StraightTest {

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
    assertEquals(new Straight(Rank.King), new Straight(cards));
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
    assertEquals(new Straight(Rank.King), new Straight(cards));
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
    assertThrows(CombinationNotPresentException.class, () -> new Straight(cards));
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
    assertEquals(new Straight(Rank.Five), new Straight(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 1, new Straight(Rank.Queen).comparesWithSame(new Straight(Rank.Jack)));
  }

}
