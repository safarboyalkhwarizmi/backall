package uz.backall.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ProductCreateDTO {
  @NotBlank(message = "Please provide name:")
  private String serialNumber;

  @NotBlank(message = "Please provide name:")
  private String name;

  @NotNull(message = "Please provide brandId:")
  private String brandName;

  @NotBlank(message = "Please provide type")
  private ProductType type;

  @NotNull(message = "Please provide your storeId")
  private Long storeId;
}