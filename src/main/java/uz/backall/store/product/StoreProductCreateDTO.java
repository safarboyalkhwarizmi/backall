package uz.backall.store.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StoreProductCreateDTO {
    private Long storeId;
    private Long productId;
    private Boolean nds;
    private Long price;
    private Long sellingPrice;
    private Integer percentage;
    private LocalDate createdDate;
    private LocalDate expiredDate;
}