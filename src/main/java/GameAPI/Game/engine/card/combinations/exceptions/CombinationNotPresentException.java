package GameAPI.Game.engine.card.combinations.exceptions;

public class CombinationNotPresentException extends IllegalStateException {
  public CombinationNotPresentException(String message) {
    super(message);
  }
}
