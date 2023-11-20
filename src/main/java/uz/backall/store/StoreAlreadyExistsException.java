package uz.backall.store;

public class StoreAlreadyExistsException extends RuntimeException {
  public StoreAlreadyExistsException(String message) {
    super(message);
  }
}