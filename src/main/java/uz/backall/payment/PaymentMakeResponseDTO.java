package uz.backall.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMakeResponseDTO {
  private String token;
  private String phone;
}
