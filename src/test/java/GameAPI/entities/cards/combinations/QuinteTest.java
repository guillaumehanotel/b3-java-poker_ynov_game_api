package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuinteTest {

  @Test
  void Quinte() {
    Cards cards = new Cards(
        new Card(Suit.HEART, Rank.King),
        new Card(Suit.HEART, Rank.Jack),
        new Card(Suit.HEART, Rank.Queen),
        new Card(Suit.HEART, Rank.Nine),
        new Card(Suit.HEART, Rank.Eight),
        new Card(Suit.HEART, Rank.Five),
        new Card(Suit.HEART, Rank.Ten)
    );
    assertEquals(new Quinte(Rank.King), new Quinte(cards));
  }

  @Test
  void comparesWithSame() {
    assertEquals((Integer) 1, new Quinte(Rank.Queen).comparesWithSame(new Quinte(Rank.Jack)));
  }

}
