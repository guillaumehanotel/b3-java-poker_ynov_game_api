package GameAPI.engine.cards.combinations;


import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;

import java.util.Comparator;

@Data
public class Brelan extends Combination {

  private static final Integer value = DoublePaire.getNextValue();
  private final Rank rank;

  static Integer getNextValue() {
    return value + 1;
  }

  Brelan(Rank rank) {
    super(value);
    this.rank = rank;
  }

  public Brelan(Cards cards) {
    super(value);
    rank = cards.getRanksByMinimumNbr(3)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("Error while creating brelan"));
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Brelan brelan = (Brelan) combination;
    return rank.compares(brelan.rank);
  }

  @Override
  public String toString() {
    return "Brelan of " + rank;
  }
}
