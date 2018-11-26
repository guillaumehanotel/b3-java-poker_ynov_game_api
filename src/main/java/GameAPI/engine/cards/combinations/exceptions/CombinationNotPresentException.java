package GameAPI.engine.cards.combinations.exceptions;

public class CombinationNotPresentException extends IllegalStateException {
  public CombinationNotPresentException(String message) {
    super(message);
  }
}
