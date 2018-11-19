package GameAPI.entities.cards.combinations.exceptions;

public class CombinationNotPresentException extends IllegalStateException {
  public CombinationNotPresentException(String message) {
    super(message);
  }
}
