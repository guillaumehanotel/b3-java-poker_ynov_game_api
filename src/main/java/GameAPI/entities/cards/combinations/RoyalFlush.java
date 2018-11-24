package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;

public class RoyalFlush extends Combination {

  static final Integer value = StraightFlush.getNextValue();

  RoyalFlush(){
    super(value);
  }

  public RoyalFlush(Cards cards) {
    super(value);
    StraightFlush straightFlush;
    try {
      straightFlush = new StraightFlush(cards);
    } catch (CombinationNotPresentException e) {
      straightFlush = null;
    }
    if (straightFlush == null || straightFlush.getBestRank() != Rank.Ace) {
      throw new CombinationNotPresentException("No Royal Flush");
    }
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    @SuppressWarnings("unused")
    RoyalFlush royalFlush = (RoyalFlush) combination;
    return 0; // RoyalFlush are always equals
  }
}
