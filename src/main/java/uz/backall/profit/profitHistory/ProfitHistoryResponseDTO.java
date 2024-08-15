package uz.backall.profit.profitHistory;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitHistoryResponseDTO {
  private Long id;
  private Long count;
  private Long profit;
  private String countType;
  private LocalDateTime createdDate;
  private Long productId;
  private Long storeId;
}