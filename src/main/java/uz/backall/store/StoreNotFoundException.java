package uz.backall.store;

public class StoreNotFoundException extends RuntimeException {
  public StoreNotFoundException(String message) {
    super(message);
  }
}