package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Card;
import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;

@EqualsAndHashCode(callSuper = true)
@Data
public class Flush extends Combination {

  private static final Integer value = Straight.getNextValue();
  private final Rank rank;

  static Integer getNextValue() {
    return value + 1;
  }

  Flush(Rank rank) {
    super(value);
    this.rank = rank;
  }

  public Flush(Cards cards) {
    super(value);
    Card bestCard = cards.stream()
        .filter(card -> cards.stream().filter(card1 -> card1.getSuit() == card.getSuit()).count() >= 5)
        .max(Comparator.comparingInt(card -> card.getRank().getValue())).orElse(null);
    if (bestCard == null) throw new CombinationNotPresentException("No Flush");
    rank = bestCard.getRank();
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Flush flush = (Flush) combination;
    return rank.compares(flush.rank);
  }

  @Override
  public String toString() {
    return "Couleur " + " par le " + rank;
  }
}
