package uz.backall.sellHistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellHistoryCreateDTO {
  private Long productId;
  private Long storeId;
  private Double count;
}