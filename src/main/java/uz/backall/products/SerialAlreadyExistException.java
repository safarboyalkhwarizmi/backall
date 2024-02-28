package uz.backall.products;

public class SerialAlreadyExistException extends RuntimeException {
  public SerialAlreadyExistException(String message) {
    super(message);
  }
}