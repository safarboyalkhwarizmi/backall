package uz.backall.store.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoreProductResponseDTO {
  private Long id;
  private Long productId;
  private Boolean nds;
  private Double price;
  private Double sellingPrice;
  private Double percentage;
  private Double count;
  private CountType countType;
}
