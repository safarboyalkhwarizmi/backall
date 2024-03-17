package uz.backall.profit.profitHistory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitHistoryInfoDTO {
  private Long id, productId;
  private Double count, profit;
  private String countType;
  private LocalDateTime createdDate;
}
