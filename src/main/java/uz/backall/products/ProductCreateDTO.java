package uz.backall.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDTO {
    private String serialNumber;
    private String name;
    private Long brandId;
}