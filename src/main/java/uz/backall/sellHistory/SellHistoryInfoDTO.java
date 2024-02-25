package uz.backall.sellHistory;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SellHistoryInfoDTO {
  private Long id, storeProductId;
  private Double count, sellingPrice;
  private String countType;
  private LocalDateTime createdDate;
}
