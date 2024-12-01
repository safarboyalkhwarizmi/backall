package uz.backall.sell.sellHistoryGroup;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SellHistoryGroupCreateDTO {
  private Long sellHistoryId;
  private Long sellGroupId;
  private Long storeId;
}