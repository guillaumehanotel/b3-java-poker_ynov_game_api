package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;

import java.util.Comparator;

@Data
public class Paire extends Combination {

  private static final Integer value = Hauteur.getNextValue();
  private final Rank rank;

  public Paire(Rank rank) {
    super(value);
    this.rank = rank;
  }

  public Paire(Cards cards) {
    super(value);
    rank = cards.getRanksByMinimumNbr(2)
        .stream()
        .max(Comparator.comparingInt(Rank::getValue))
        .orElseThrow(() -> new CombinationNotPresentException("bad paire creation"));
  }

  static Integer getNextValue() {
    return value + 1;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Paire paire = (Paire) combination;
    return rank.compares(paire.rank);
  }

  @Override
  public String toString() {
    return "Paire of " + rank;
  }

}
