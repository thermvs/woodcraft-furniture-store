package store.furniture.exception;

public class FurnitureDoesNotExistException extends DoesNotExistException {
  public FurnitureDoesNotExistException(Long id) {
    super(String.format(("furniture %s does not exist"), id));
  }
}
