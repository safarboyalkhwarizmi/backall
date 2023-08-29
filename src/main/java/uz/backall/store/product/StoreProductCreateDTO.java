package uz.backall.store.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreProductCreateDTO {
    private Long storeId;
    private Long productId;
    private Boolean nds;
    private Long price;
    private Long sellingPrice;
    private Integer percentage;
}