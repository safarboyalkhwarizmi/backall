package uz.backall.profit.profitHistoryGroup;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfitHistoryGroupResponseDTO {
  private Long id;
  private Long profitGroupId;
  private Long profitHistoryId;
}