package uz.backall.profit.profitAmountDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitAmountDateCreateDTO {
  private String date;
  private Double amount;
  private Long storeId;
}