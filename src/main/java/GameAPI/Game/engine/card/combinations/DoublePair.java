package GameAPI.Game.engine.card.combinations;

import GameAPI.Game.engine.card.Card;
import GameAPI.Game.engine.card.Cards;
import GameAPI.Game.engine.card.Rank;
import GameAPI.Game.engine.card.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
public class DoublePair extends Combination {

  private static final Integer value = Pair.getNextValue();
  private final List<Rank> ranks;


  public DoublePair(Rank rank1, Rank rank2) {
    super(value);
    ranks = Stream.of(rank1, rank2)
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .collect(Collectors.toList());
  }

  public DoublePair(Cards cards) {
    super(value);
    List<Rank> pairs = new ArrayList<>();
    Map<Rank, Integer> rankNbr = new HashMap<>();
    for (Card card : cards) {
      if (!rankNbr.containsKey(card.getRank())) {
        rankNbr.put(card.getRank(), 0);
      }
      rankNbr.put(card.getRank(), rankNbr.get(card.getRank()) + 1);
      if (rankNbr.get(card.getRank()) >= 2) {
        pairs.add(card.getRank());
        rankNbr.remove(card.getRank());
      }
    }
    List<Rank> collect = pairs.stream()
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .limit(2)
        .collect(Collectors.toList());
    if (collect.size() < 2) {
      throw new CombinationNotPresentException("Illegal DoublePair ranks");
    }
    ranks = collect;
  }

  static Integer getNextValue() {
    return value + 1;
  }

  public List<Rank> getRanksForTest() {
    return ranks;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    DoublePair doublePair = (DoublePair) combination;
    Integer compares = ranks.get(0).compares(doublePair.ranks.get(0));
    if (compares == 0) compares = ranks.get(1).compares(doublePair.ranks.get(1));
    return compares;
  }

  @Override
  public String toString() {
    return "Double paire de " + ranks.get(0) + " par les " + ranks.get(1);
  }
}
