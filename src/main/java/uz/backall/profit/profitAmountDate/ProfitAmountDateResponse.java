package uz.backall.profit.profitAmountDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfitAmountDateResponse {
  private Long id;
  private String date;
  private Double amount;
}