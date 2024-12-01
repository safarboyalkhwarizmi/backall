package uz.backall.profit.profitHistory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class ProfitHistoryCreateDTO {
  private Long productId;
  private Long count;
  private String countType;
  private Long storeId;
  private Long profit;
  private LocalDateTime createdDate;
}