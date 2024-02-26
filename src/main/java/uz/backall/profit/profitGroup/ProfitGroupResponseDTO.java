package uz.backall.profit.profitGroup;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitGroupResponseDTO {
  private Long id;
  private LocalDateTime createdDate;
  private Double profit;
  private Long storeId;
}