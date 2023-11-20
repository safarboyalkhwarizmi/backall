package uz.backall.store.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreProductInfoDTO {
  private Long productId;
  private String name;
  private String productCount;
}
