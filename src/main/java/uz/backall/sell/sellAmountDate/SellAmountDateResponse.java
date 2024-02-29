package uz.backall.sell.sellAmountDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellAmountDateResponse {
  private Long id;

  private String date;

  private Double amount;
}