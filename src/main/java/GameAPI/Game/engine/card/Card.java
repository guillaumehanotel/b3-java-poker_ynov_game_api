package GameAPI.Game.engine.card;

public class Card {

  private final Rank rank;
  private final Suit suit;

  public Card(Suit suit, Rank rank) {
    this.rank = rank;
    this.suit = suit;
  }

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  @Override
  public String toString() {
    return rank + " of " + suit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    if (rank != card.rank) return false;
    return suit == card.suit;
  }

  @Override
  public int hashCode() {
    int result = rank != null ? rank.hashCode() : 0;
    result = 31 * result + (suit != null ? suit.hashCode() : 0);
    return result;
  }
}
