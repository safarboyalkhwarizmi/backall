package uz.backall.products; // Assuming ProductResponseDTO is in this package

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponseDTO {
  private Long id;
  private String name;
  private String serialNumber;
  private String brandName;
}