package uz.backall.profit.profitHistoryGroup;

import lombok.Getter;
import lombok.Setter;
import uz.backall.profit.profitHistory.ProfitHistoryResponseDTO;

@Getter
@Setter
public class ProfitHistoryLinkInfoDTO {
  private Long id;
  private Long profitGroupId;
  private Long profitHistoryId;

  private ProfitHistoryResponseDTO profitHistory;
}