package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;

@EqualsAndHashCode(callSuper = true)
@Data
public class Pair extends Combination {

  private static final Integer value = HighHand.getNextValue();
  private final Rank rank;

  public Pair(Rank rank) {
    super(value);
    this.rank = rank;
  }

  public Pair(Cards cards) {
    super(value);
    rank = cards.getRanksByMinimumNbr(2)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("bad pair creation"));
  }

  static Integer getNextValue() {
    return value + 1;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Pair pair = (Pair) combination;
    return rank.compares(pair.rank);
  }

  @Override
  public String toString() {
    return "Paire de " + rank;
  }

}
