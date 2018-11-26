package GameAPI.engine.card.combinations;

import GameAPI.engine.card.Card;
import GameAPI.engine.card.Cards;
import GameAPI.engine.card.Rank;
import GameAPI.engine.card.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class DoublePaire extends Combination {

  private static final Integer value = Paire.getNextValue();
  private final List<Rank> ranks;


  public DoublePaire(Rank rank1, Rank rank2) {
    super(value);
    ranks = Stream.of(rank1, rank2)
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .collect(Collectors.toList());
  }

  public DoublePaire(Cards cards) {
    super(value);
    List<Rank> paires = new ArrayList<>();
    Map<Rank, Integer> rankNbr = new HashMap<>();
    for (Card card : cards) {
      if (!rankNbr.containsKey(card.getRank())) {
        rankNbr.put(card.getRank(), 0);
      }
      rankNbr.put(card.getRank(), rankNbr.get(card.getRank()) + 1);
      if (rankNbr.get(card.getRank()) >= 2) {
        paires.add(card.getRank());
        rankNbr.remove(card.getRank());
      }
    }
    List<Rank> collect = paires.stream()
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .limit(2)
        .collect(Collectors.toList());
    if (collect.size() < 2) {
      throw new CombinationNotPresentException("Illegal DoublePaire ranks");
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
    DoublePaire doublePaire = (DoublePaire) combination;
    Integer compares = ranks.get(0).compares(doublePaire.ranks.get(0));
    if (compares == 0) compares = ranks.get(1).compares(doublePaire.ranks.get(1));
    return compares;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " of " + ranks.get(0) + " and " + ranks.get(1);
  }
}
