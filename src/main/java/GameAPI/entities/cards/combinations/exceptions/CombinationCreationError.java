package GameAPI.entities.cards.combinations.exceptions;

public class CombinationCreationError extends IllegalStateException {
  public CombinationCreationError(String message) {
    super(message);
  }
}
