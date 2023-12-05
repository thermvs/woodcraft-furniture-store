package store.furniture.exception;

public class WoodDoesNotExistException extends DoesNotExistException {
  public WoodDoesNotExistException(Long id) {
    super(String.format(("wood %s does not exist"), id));
  }
}


