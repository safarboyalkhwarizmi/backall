package uz.backall.sell.sellHistoryGroup;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellHistoryGroupResponseDTO {
  private Long id;
  private Long sellGroupId;
  private Long sellHistoryId;
}