package GameAPI.entities.cards.combinations;

import GameAPI.entities.cards.Card;
import GameAPI.entities.cards.Cards;
import GameAPI.entities.cards.Rank;
import GameAPI.entities.cards.Suit;
import GameAPI.entities.cards.combinations.exceptions.CombinationNotPresentException;
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
