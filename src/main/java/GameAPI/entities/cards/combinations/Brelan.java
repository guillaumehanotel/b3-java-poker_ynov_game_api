package GameAPI.entities.cards.combinations;


import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;

import java.util.Comparator;

public class Brelan extends Combination {
  private static final Integer value = DoublePaire.getNextValue();

  Brelan(Cards cards) {
    super(value);
    cards.getRanksByMinimumNbr(3)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("Error while creating brelan"));
  }

  @Override
  Integer comparesWithSame(Combination combination) {
    return null;
  }
}
