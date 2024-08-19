package uz.backall.sell.sellHistoryGroup;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SellHistoryDetailDTO {
  private Long id;
  private String productName;
  private String count_type;
  private Boolean saved;
  private Long selling_price;
  private Long count;
  private LocalDateTime created_date;
  private Long product_id;
}