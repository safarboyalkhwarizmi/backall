package uz.backall.sell.sellHistoryGroup;

import lombok.Getter;
import lombok.Setter;
import uz.backall.sell.sellHistory.SellHistoryResponseDTO;

@Getter
@Setter
public class SellHistoryLinkInfoDTO {
  private Long id;
  private Long sellGroupId;
  private Long sellHistoryId;

  private SellHistoryResponseDTO sellHistory;
}