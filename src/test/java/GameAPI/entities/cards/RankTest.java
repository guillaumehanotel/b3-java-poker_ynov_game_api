package GameAPI.entities.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RankTest {

  @Test
  void comparesAceAndTwo() {
    assertTrue(Rank.Ace.getValue() > Rank.Two.getValue());
  }

}
