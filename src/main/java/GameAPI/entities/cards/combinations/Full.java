package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationCreationError;

import java.util.Comparator;

public class Full extends Combination {

  private static Integer value = DoublePaire.getNextValue();
  private Rank doubleRank;
  private Rank tripletRank;

  Full(Rank tripletRank, Rank doubleRank) {
    super(value);
    this.tripletRank = tripletRank;
    this.doubleRank = doubleRank;
  }

  public Full(Cards cards) throws CombinationCreationError {
    super(value);
    tripletRank = cards.getRanksByMinimumNbr(3)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationCreationError("Full triplet is null"));
    doubleRank = cards.getRanksByMinimumNbr(2)
        .stream()
        .filter(rank -> rank != tripletRank)
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationCreationError("Full double is null"));
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Full full = (Full) combination;
    if (tripletRank.getValue() > full.tripletRank.getValue()) {
      return 1;
    } else if (tripletRank.getValue() < full.tripletRank.getValue()) {
      return -1;
    } else if (doubleRank.getValue() > full.doubleRank.getValue()) {
      return 1;
    } else if (doubleRank.getValue() < full.doubleRank.getValue()) {
      return -1;
    }
    return 0;
  }
}
