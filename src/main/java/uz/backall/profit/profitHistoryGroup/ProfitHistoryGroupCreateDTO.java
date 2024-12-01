package uz.backall.profit.profitHistoryGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ProfitHistoryGroupCreateDTO {
  private Long profitHistoryId;
  private Long profitGroupId;
  private Long storeId;
}