package uz.backall.sell.sellHistory;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SellHistoryInfoDTO {
  private Long id, productId;
  private Long count, sellingPrice;
  private String countType;
  private LocalDateTime createdDate;
}
