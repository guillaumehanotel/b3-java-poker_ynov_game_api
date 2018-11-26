package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;

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

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
