package uz.backall.idempotencyKey;

import lombok.Getter;

@Getter
public class IdempotencyKeyUsedException extends RuntimeException {
  private final String code = "used_key";

  public IdempotencyKeyUsedException(String message) {
    super(message);
  }
}