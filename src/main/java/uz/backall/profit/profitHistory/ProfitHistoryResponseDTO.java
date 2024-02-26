package uz.backall.profit.profitHistory;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitHistoryResponseDTO {
  private Long id;
  private Double count;
  private Double profit;
  private String countType;
  private LocalDateTime createdDate;
  private Long productId;
  private Long storeId;
}