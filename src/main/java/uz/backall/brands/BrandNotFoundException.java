package uz.backall.brands;

public class BrandNotFoundException extends RuntimeException {
  public BrandNotFoundException(String message) {
    super(message);
  }
}