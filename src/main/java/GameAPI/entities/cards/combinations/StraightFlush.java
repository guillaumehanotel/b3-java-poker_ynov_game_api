package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;

import java.util.stream.Collectors;

@Data
public class StraightFlush extends Combination {

  private static final Integer value = FourOfAKind.getNextValue();
  private final Rank bestRank;

  public static Integer getNextValue() {
    return value + 1;
  }

  StraightFlush(Rank bestRank) {
    super(value);
    this.bestRank = bestRank;
  }

  public StraightFlush(Cards cards) {
    super(value);
    // Keep cards whose suit is present at least 5 times
    Cards fiveTimesSuitCards = new Cards();
    fiveTimesSuitCards.addAll(cards.stream()
        .filter(card -> cards.stream().map(Card::getSuit).filter(suit -> suit == card.getSuit()).count() >= 5)
        .collect(Collectors.toList()));
    try {
      Quinte bestQuinte = new Quinte(fiveTimesSuitCards);
      bestRank = bestQuinte.getBestRank();
    } catch (CombinationNotPresentException | IndexOutOfBoundsException e) {
      throw new CombinationNotPresentException("No straight flush");
    }
  }

  public Rank getBestRank() {
    return bestRank;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    StraightFlush straightFlush = (StraightFlush) combination;
    return bestRank.compares(straightFlush.bestRank);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " with best rank : " + bestRank;
  }
}
