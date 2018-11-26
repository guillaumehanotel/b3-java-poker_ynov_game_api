package GameAPI.engine.cards.combinations;

import GameAPI.engine.cards.Card;
import GameAPI.engine.cards.Cards;
import GameAPI.engine.cards.Rank;
import GameAPI.engine.cards.Suit;
import GameAPI.engine.cards.combinations.exceptions.CombinationNotPresentException;
import lombok.Data;

import java.util.Comparator;

@Data
public class Couleur extends Combination {

  private static final Integer value = Quinte.getNextValue();
  private final Suit suit;
  private final Rank rank;

  static Integer getNextValue() {
    return value + 1;
  }

  Couleur(Suit suit, Rank rank) {
    super(value);
    this.suit = suit;
    this.rank = rank;
  }

  public Couleur(Cards cards) {
    super(value);
    Card bestCard = cards.stream()
        .filter(card -> cards.stream().filter(card1 -> card1.getSuit() == card.getSuit()).count() >= 5)
        .max(Comparator.comparingInt(card -> card.getRank().getValue())).orElse(null);
    if (bestCard == null) throw new CombinationNotPresentException("No Couleur");
    suit = bestCard.getSuit();
    rank = bestCard.getRank();
  }

  @Override
  protected Integer comparesWithSame(Combination combination) {
    Couleur couleur = (Couleur) combination;
    return rank.compares(couleur.rank);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " by " + rank;
  }
}
