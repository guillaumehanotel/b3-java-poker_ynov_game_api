package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;

import java.util.Comparator;

public class Full extends Combination {

  private static Integer value = Couleur.getNextValue();
  private Rank doubleRank;
  private Rank tripletRank;

  public static Integer getNextValue() {
    return value;
  }

  Full(Rank tripletRank, Rank doubleRank) {
    super(value);
    this.tripletRank = tripletRank;
    this.doubleRank = doubleRank;
  }

  public Full(Cards cards) throws CombinationNotPresentException {
    super(value);
    tripletRank = cards.getRanksByMinimumNbr(3)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("Full triplet is null"));
    doubleRank = cards.getRanksByMinimumNbr(2)
        .stream()
        .filter(rank -> rank != tripletRank)
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("Full double is null"));
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
