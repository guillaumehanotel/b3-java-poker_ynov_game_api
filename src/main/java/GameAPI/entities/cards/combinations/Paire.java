package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.combinations.exceptions.CombinationCreationError;

import java.util.Comparator;
import java.util.Objects;

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
        .orElseThrow(() -> new CombinationCreationError("bad paire creation"));
  }

  static Integer getNextValue() {
    return value + 1;
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Paire paire = (Paire) combination;
    if (rank.getValue() > paire.rank.getValue()) {
      return 1;
    } else if (rank.getValue() < paire.rank.getValue()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "Paire of " + rank;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank);
  }
}
