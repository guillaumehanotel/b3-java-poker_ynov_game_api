package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Card;
import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class Straight extends Combination {

  private static final Integer value = ThreeOfAKind.getNextValue();
  private final Rank bestRank;

  static Integer getNextValue() {
    return value + 1;
  }

  public Straight(Rank lastRank) {
    super(value);
    this.bestRank = lastRank;
  }

  public Straight(Cards cards) {
    super(value);
    List<Rank> collect = cards.stream()
        .map(Card::getRank)
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .distinct()
        .collect(Collectors.toList());
    Rank potentialRank = collect.get(0);
    int nbrCorrect = 1;
    for (int i = 1; i < collect.size(); i++) {
      Rank rank1 = collect.get(i - 1);
      Rank rank2 = collect.get(i);
      boolean areSequentials = rank1.getValue() - rank2.getValue() == 1;
      boolean twoAndAce = rank2 == Rank.Two && collect.get(0) == Rank.Ace;
      if (areSequentials || twoAndAce) {
        if (twoAndAce) nbrCorrect++;
        nbrCorrect += 1;
      } else {
        potentialRank = rank2;
        nbrCorrect = 1;
      }
      if (nbrCorrect == 5) {
        bestRank = potentialRank;
        return;
      }
    }
    throw new CombinationNotPresentException("No Straight");
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Straight straight = (Straight) combination;
    return this.bestRank.compares(straight.bestRank);
  }

  @Override
  public String toString() {
    return "Suite par le " + bestRank;
  }

  public Rank getBestRank() {
    return bestRank;
  }
}
