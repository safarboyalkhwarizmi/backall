package uz.backall.brands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.brands.localStoreBrand.LocalStoreBrandEntity;
import uz.backall.brands.localStoreBrand.LocalStoreBrandRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {
  private final BrandRepository brandRepository;
  private final LocalStoreBrandRepository localStoreBrandRepository;

  public BrandResponseDTO create(Long storeId, String brandName) {
    Optional<LocalStoreBrandEntity> byBrandNameAndStoreId =
      localStoreBrandRepository.findByBrand_NameAndStoreId(brandName, storeId);
    if (byBrandNameAndStoreId.isEmpty()) {
      BrandEntity brand = new BrandEntity();
      brand.setName(brandName);
      brandRepository.save(brand);

      LocalStoreBrandEntity localStoreBrandEntity = new LocalStoreBrandEntity();
      localStoreBrandEntity.setBrandId(brand.getId());
      localStoreBrandEntity.setStoreId(storeId);
      localStoreBrandRepository.save(localStoreBrandEntity);

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