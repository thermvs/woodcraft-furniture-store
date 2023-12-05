package store.furniture.exception;

public class DatabaseConflictException extends Exception {
  public DatabaseConflictException(String message) {
    super(message);
  }
}
