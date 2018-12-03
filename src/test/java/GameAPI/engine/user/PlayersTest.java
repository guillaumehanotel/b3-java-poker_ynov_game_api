package GameAPI.engine.user;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.Suit;
import GameAPI.engine.card.combinations.*;
import GameAPI.engine.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayersTest {

  private Players players;

  @BeforeEach
  void setUp() {
    Game game = Mockito.mock(Game.class);
    Mockito.when(game.getLastCommunityCards()).thenReturn(Collections.emptyList());
    Mockito.when(game.getCombinationTypes()).thenReturn(Arrays.asList(
        HighHand.class,
        Pair.class,
        DoublePair.class,
        ThreeOfAKind.class,
        Straight.class,
        Flush.class,
        FullHouse.class,
        FourOfAKind.class,
        StraightFlush.class,
        RoyalFlush.class
    ));
    List<Player> playersList = Arrays.asList(
        new Player(new User("mail", "p1", 10000), 1000, game),
        new Player(new User("mail", "p2", 10000), 1000, game),
        new Player(new User("mail", "p3", 10000), 1000, game)
    );
    players = new Players(playersList, 0);
  }


  @Test
  void getPlayingPlayer() {
    final Player expectedPlayer = players.get(0);
    final Player actualPlayer = players.getPlayingPlayer();

    assertEquals(expectedPlayer, actualPlayer);
  }

  @Test
  void getPlayersByResultOneWinner() {
    players.get(0).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Eight), new Card(Suit.HEART, Rank.Seven)));
    players.get(1).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Ace), new Card(Suit.HEART, Rank.Ace)));
    players.get(2).setDownCards(Arrays.asList(new Card(Suit.DIAMOND, Rank.Eight), new Card(Suit.SPADE, Rank.Seven)));
    Player actual = players.getPlayersByResult().get(PlayerStatus.WINNER).get(0);
    Player expected = players.get(1);
    assertEquals(expected, actual);
  }

  @Test
  void getPlayersByResultMultipleWinners() {
    players.get(0).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Eight), new Card(Suit.HEART, Rank.Seven)));
    players.get(1).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Ace), new Card(Suit.HEART, Rank.Ace)));
    players.get(2).setDownCards(Arrays.asList(new Card(Suit.SPADE, Rank.Ace), new Card(Suit.DIAMOND, Rank.Ace)));
    List<Player> actual = players.getPlayersByResult().get(PlayerStatus.WINNER);
    List<Player> expected = Arrays.asList(players.get(1), players.get(2));
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void getPlayersByResultWinnerLastNotDropped() {
    players.get(0).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Eight), new Card(Suit.HEART, Rank.Seven)));
    players.get(1).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Ace), new Card(Suit.HEART, Rank.Ace)));
    players.get(2).setDownCards(Arrays.asList(new Card(Suit.DIAMOND, Rank.Eight), new Card(Suit.SPADE, Rank.Seven)));
    players.get(1).setHasFolded(true);
    players.get(2).setHasFolded(true);
    Player actual = players.getPlayersByResult().get(PlayerStatus.WINNER).get(0);
    Player expected = players.get(0);
    assertEquals(expected, actual);
  }

  @Test
  void getPlayersByResultLooser() {
    players.get(0).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Eight), new Card(Suit.HEART, Rank.Seven)));
    players.get(1).setDownCards(Arrays.asList(new Card(Suit.CLUB, Rank.Ace), new Card(Suit.HEART, Rank.Ace)));
    players.get(2).setDownCards(Arrays.asList(new Card(Suit.DIAMOND, Rank.Eight), new Card(Suit.SPADE, Rank.Seven)));
    List<Player> actual = players.getPlayersByResult().get(PlayerStatus.LOOSER);
    List<Player> expected = Arrays.asList(players.get(0), players.get(2));
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  void getNextPlayingPlayer() {
    players.add(0, Mockito.mock(Player.class));
    Mockito.when(players.get(0).isIgnoredForRound()).thenReturn(true);
    Player expected = players.get(1);
    Player actual = players.getNextPlayingPlayer();
    assertEquals(expected, actual);
  }

  @Test
  void haveAllFoldExceptOne() {
    players.get(0).fold();
    players.get(1).fold();
    assertEquals(true, players.haveAllFoldedExceptOne());
  }

  @Test
  void haveAllPlayed() {
    for (Player player : players) {
      player.setHasPlayedTurn(true);
    }
    Player mock = Mockito.mock(Player.class);
    Mockito.when(mock.isIgnoredForRound()).thenReturn(true);
    players.add(mock);
    assertEquals(true, players.haveAllPlayed());
  }

  @Test
  void haveAllEqualBet() {
    for (Player player : players) {
      player.setCurrentBet(42);
    }
    Player mock = Mockito.mock(Player.class);
    Mockito.when(mock.isIgnoredForRound()).thenReturn(true);
    players.add(mock);
    assertEquals(true, players.haveAllEqualBet());
  }

  @Test
  void initPlayingPlayerPosition() {
    Player mock = Mockito.mock(Player.class);
    Mockito.when(mock.isEliminated()).thenReturn(true);
    players.add(0, mock);
    players.initPlayingPlayerPosition();
    assertEquals(players.get(1), players.getPlayingPlayer());
  }
}
