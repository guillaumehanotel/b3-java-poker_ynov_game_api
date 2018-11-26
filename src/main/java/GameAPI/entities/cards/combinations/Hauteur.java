package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Hauteur extends Combination {
  private static Integer value = 0;
  private List<Rank> ranks;

  Hauteur(Collection<Rank> ranks) {
    super(value);
    this.ranks = ranks
        .stream()
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .collect(Collectors.toList());
  }

  public Hauteur(Cards cards) {
    super(value);
    this.ranks = cards
        .stream()
        .map(Card::getRank)
        .sorted(Collections.reverseOrder(Comparator.comparingInt(Rank::getValue)))
        .collect(Collectors.toList());
  }

  static Integer getNextValue() {
    return value + 1;
  }

  List<Rank> getRanksForTest() {
    return ranks;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Hauteur hauteur = (Hauteur) combination;
    if (ranks.size() != hauteur.ranks.size()) throw new RuntimeException("Can't compare hauteurs of different sizes");
    for (int i = 0; i < ranks.size(); i++) {
      Integer compares = ranks.get(i).compares(hauteur.ranks.get(i));
      if (compares != 0) return compares;
    }
    return 0;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " with " + ranks;
  }
}
