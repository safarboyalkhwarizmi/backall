package uz.backall.sell.sellGroup;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SellGroupResponseDTO {
  private Long id;
  private LocalDateTime createdDate;
  private Long amount;
  private Long storeId;
}