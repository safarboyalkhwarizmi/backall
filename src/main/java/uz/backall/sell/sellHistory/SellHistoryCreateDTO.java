package uz.backall.sell.sellHistory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class SellHistoryCreateDTO {
  private Long productId;
  private Long storeId;
  private Long count;
  private String countType;
  private Long sellingPrice;
  private LocalDateTime createdDate;
}