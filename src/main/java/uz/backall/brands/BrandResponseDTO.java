package uz.backall.brands;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BrandResponseDTO {
  private Long id;
  private String name;
}