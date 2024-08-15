package uz.backall.profit.profitHistoryGroup;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfitHistoryDetailDTO {
  private Long id;
  private String name;
  private String count_type;
  private Boolean saved;
  private Long profit;
  private Long count;
  private LocalDateTime created_date;
  private Long product_id;
}