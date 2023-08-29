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
    private Double price;
    private Double sellingPrice;
    private Double percentage;
    private Double count;
    private CountType countType;

    /* TODO FOR FIRST OCTOBER 2023 SUNDAY
    private LocalDate createdDate;
    private LocalDate expiredDate;
    */

}