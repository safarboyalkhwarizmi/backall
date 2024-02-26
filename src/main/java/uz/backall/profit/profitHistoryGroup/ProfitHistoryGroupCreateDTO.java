package uz.backall.profit.profitHistoryGroup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitHistoryGroupCreateDTO {
  private Long profitHistoryId;
  private Long profitGroupId;
  private Long storeId;
}