package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Quinte extends Combination {

  private static final Integer value = Brelan.getNextValue();
  private final Rank lastRank;

  static Integer getNextValue() {
    return value + 1;
  }

  public Quinte(Rank lastRank) {
    super(value);
    this.lastRank = lastRank;
  }

  public Quinte(Cards cards) {
    // todo ace as first card
    super(value);
    List<Card> collect = cards.stream()
        .sorted(Collections.reverseOrder(Comparator.comparingInt(card -> card.getRank().getValue())))
        .collect(Collectors.toList());
    Rank potentialRank = collect.get(0).getRank();
    int nbrCorrect = 1;
    for (int i = 1; i < collect.size(); i++) {
      if (collect.get(i-1).getRank().getValue() - collect.get(i).getRank().getValue() != 1) {
        potentialRank = collect.get(i).getRank();
        nbrCorrect = 1;
      } else nbrCorrect += 1;
      if (nbrCorrect == 5) {
        lastRank = potentialRank;
        return;
      }
    }
    // todo combinationnotpresentexception
    throw new RuntimeException("No Quinte");
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Quinte quinte = (Quinte) combination;
    return this.lastRank.compares(quinte.lastRank);
  }

  @Override
  public String toString() {
    return "Quinte starting at " + lastRank;
  }
}
