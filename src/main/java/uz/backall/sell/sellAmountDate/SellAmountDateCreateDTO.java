package uz.backall.sell.sellAmountDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellAmountDateCreateDTO {
  private String date;
  private Double amount;
  private Long storeId;
}