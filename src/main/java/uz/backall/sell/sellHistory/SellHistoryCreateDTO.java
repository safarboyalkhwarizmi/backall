package uz.backall.sell.sellHistory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SellHistoryCreateDTO {
  private Long productId;
  private Long storeId;
  private Double count;
  private String countType;
  private Double sellingPrice;
  private LocalDateTime createdDate;
}