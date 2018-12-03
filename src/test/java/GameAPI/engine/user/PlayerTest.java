package GameAPI.engine.user;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.Combination;
import GameAPI.engine.card.combinations.DoublePair;
import GameAPI.engine.card.combinations.HighHand;
import GameAPI.engine.card.combinations.Pair;
import GameAPI.engine.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  private Player player;
  private Game game;

  @BeforeEach
  void setUp() {
    game = Mockito.mock(Game.class);
    player = new Player(new User("1", "2", 1000), 100, game);
  }

  @Test
  void getBestCombination() {
    Mockito.when(game.getLastCommunityCards()).thenReturn(
        Arrays.asList(new Card(Suit.DIAMOND, Rank.King), new Card(Suit.CLUB, Rank.Four))
    );
    Mockito.when(game.getCombinationTypes()).thenReturn(Arrays.asList(HighHand.class, Pair.class, DoublePair.class));
    player.setDownCards(Arrays.asList(new Card(Suit.HEART, Rank.King), new Card(Suit.SPADE, Rank.Four)));

    Combination expected = new DoublePair(Rank.King, Rank.Four);
    Combination actual = player.getBestCombinationForTest();
    assertEquals(expected, actual);
  }

  @Test
  void resetRound() {
    player.resetRound();
    assertFalse(player.getHasDropped());
    assertArrayEquals(Collections.emptyList().toArray(), player.getDownCards().toArray());
    assertEquals((Integer) 0, player.getCurrentBet());
    assertArrayEquals(Collections.emptyList().toArray(), player.getRoles().toArray());
  }

  @Test
  void resetTurn() {
    player.resetTurn();
    assertFalse(player.getHasPlayedTurn());
  }

  @Test
  void bets() {
    player.setChips(100);
    player.bets(42);
    assertEquals((Integer) 58, player.getChips());
    assertEquals((Integer) 42, player.getCurrentBet());
    Mockito.verify(game, Mockito.times(1)).addToPot(42);
    assertTrue(player.getHasPlayedTurn());
  }

  @Test
  void fold() {
    player.fold();
    assertTrue(player.getHasDropped());
    assertTrue(player.getHasPlayedTurn());
  }

  @Test
  void call() {
    player.setChips(100);
    player.setCurrentBet(0);
    player.call(42);
    assertEquals((Integer) 58, player.getChips());
    assertEquals((Integer) 42, player.getCurrentBet());
  }

  @Test
  void callTooMuch() {
    player.setChips(30);
    player.setCurrentBet(0);
    player.call(42);
    assertEquals((Integer) 0, player.getChips());
    assertEquals((Integer) 30, player.getCurrentBet());
  }

  @Test
  void isIgnoredForRound() {
    player.setIsEliminated(false);
    player.setChips(0);
    player.setHasDropped(false);
    assertTrue(player.isIgnoredForRound());
  }

  @Test
  void win() {
    player.setChips(7);
    player.getUser().setMoney(0);
    player.win(42);
    assertEquals((Integer) 49, player.getChips());
    assertNull(player.get_combination());
    assertEquals((Integer) 42, player.getEarnedMoney());
    assertEquals((Integer) 42, player.getUser().getMoney());
  }

  @Test
  void loose() {
    player.setChips(0);
    player.loose();
    assertTrue(player.getIsEliminated());
    assertNull(player.get_combination());
  }

  @Test
  void savePreviousDownCards() {
    player.setDownCards(Collections.singletonList(new Card(Suit.HEART, Rank.Queen)));
    player.savePreviousDownCards();
    assertArrayEquals(new Card[]{new Card(Suit.HEART, Rank.Queen)}, player.getDownCards().toArray());
  }

  @Test
  void comparesCards() {
    Mockito.when(game.getCombinationTypes()).thenReturn(Collections.singletonList(HighHand.class));
    player.setDownCards(Collections.singletonList(new Card(Suit.HEART, Rank.Queen)));

    Player otherPlayer = new Player(new User("1", "other", 100), 100, game);
    otherPlayer.setDownCards(Collections.singletonList(new Card(Suit.HEART, Rank.Jack)));

    Integer comparisonResult = player.comparesCards(otherPlayer);
    assertEquals((Integer) 1, comparisonResult);
  }
}
