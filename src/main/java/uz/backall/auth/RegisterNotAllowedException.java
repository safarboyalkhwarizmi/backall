package uz.backall.auth;

public class RegisterNotAllowedException extends RuntimeException {
  public RegisterNotAllowedException(String message) {
    super(message);
  }
}