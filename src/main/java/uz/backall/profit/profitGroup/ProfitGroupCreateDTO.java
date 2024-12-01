package uz.backall.profit.profitGroup;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class ProfitGroupCreateDTO {
  @NotNull
  private LocalDateTime createdDate;

  @NotNull
  private Long storeId;

  @NotNull
  private Long profit;
}