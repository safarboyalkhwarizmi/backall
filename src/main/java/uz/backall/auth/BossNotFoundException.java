package uz.backall.auth;

public class BossNotFoundException extends RuntimeException {
  public BossNotFoundException(String message) {
    super(message);
  }
}