package uz.backall.brands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {
  private final BrandRepository brandRepository;

  public BrandResponseDTO create(String brandName) {
    Optional<BrandEntity> byName = brandRepository.findByName(brandName);
    if (byName.isEmpty()) {
      BrandEntity brand = new BrandEntity();
      brand.setName(brandName);
      brandRepository.save(brand);
      return new BrandResponseDTO(brand.getId(), brand.getName());
    }

    throw new BrandAlreadyExistsException("Brand already exits");
  }

  public List<BrandResponseDTO> getAll() {
    List<BrandEntity> all = brandRepository.findAll();

    List<BrandResponseDTO> result = new LinkedList<>();
    for (BrandEntity brand : all) {
      BrandResponseDTO response =
        new BrandResponseDTO(brand.getId(), brand.getName());
      result.add(response);
    }

    return result;
  }
}