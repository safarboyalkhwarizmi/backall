package uz.backall.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequestDTO {
  private String token;
  private String code;
}
