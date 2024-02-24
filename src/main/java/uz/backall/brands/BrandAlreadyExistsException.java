package uz.backall.brands;

public class BrandAlreadyExistsException extends RuntimeException {
  public BrandAlreadyExistsException(String message) {
    super(message);
  }
}