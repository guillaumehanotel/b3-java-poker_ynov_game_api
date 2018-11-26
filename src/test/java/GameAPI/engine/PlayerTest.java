package GameAPI.engine;

import GameAPI.engine.cards.Card;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.Suit;
import GameAPI.engine.cards.combinations.Combination;
import GameAPI.engine.cards.combinations.DoublePaire;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

  @Test
  void getBestCombination() {
    Game game = Mockito.mock(Game.class);
    Mockito.when(game.getLastCommunityCards()).thenReturn(Arrays.asList(new Card(Suit.DIAMOND, Rank.King), new Card(Suit.CLUB, Rank.Four)));
    Player player = new Player(
        new User("1", "2", 1000),
        10000,
        game
    );
    player.setDownCards(Arrays.asList(new Card(Suit.HEART, Rank.King), new Card(Suit.SPADE, Rank.Four)));
    Combination expected = new DoublePaire(Rank.King, Rank.Four);
    Combination actual = player.getBestCombination();
    assertEquals(expected, actual);
  }
}
