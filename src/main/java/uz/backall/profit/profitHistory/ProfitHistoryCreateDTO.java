package uz.backall.profit.profitHistory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitHistoryCreateDTO {
  private Long productId;
  private Long storeId;
  private Double count;
  private String countType;
  private Double profit;
  private LocalDateTime createdDate;
}