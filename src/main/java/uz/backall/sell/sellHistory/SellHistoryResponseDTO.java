package uz.backall.sell.sellHistory;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SellHistoryResponseDTO {
  private Long id;
  private Long count;
  private Long sellingPrice;
  private String countType;
  private LocalDateTime createdDate;
  private Long productId;
  private Long storeId;
}