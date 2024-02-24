package uz.backall.brands;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.brands.localStoreBrand.LocalStoreBrandEntity;
import uz.backall.brands.localStoreBrand.LocalStoreBrandRepository;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductResponseDTO;
import uz.backall.products.localStoreProduct.LocalStoreProductEntity;

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

  public Page<BrandResponseDTO> getGlobalPage(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    // Retrieve the paginated list of LocalStoreProductEntity objects using the repository
    Page<BrandEntity> brandEntities = brandRepository.findByBrandType(
      BrandType.GLOBAL, pageable
    );

    // Map ProductEntity objects to ProductResponseDTO objects
    Page<BrandResponseDTO> responsePage = brandEntities.map(brandEntity ->
      new BrandResponseDTO(
        brandEntity.getId(),
        brandEntity.getName()
      )
    );

    return responsePage;
  }
  public Page<BrandResponseDTO> getLocalPage(Long storeId, Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<BrandEntity> byStoreId = localStoreBrandRepository.findByStoreId(
      storeId, pageable
    ).map(LocalStoreBrandEntity::getBrand);

    Page<BrandResponseDTO> responsePage = byStoreId.map(brandEntity ->
      new BrandResponseDTO(
        brandEntity.getId(),
        brandEntity.getName()
      )
    );

    return responsePage;
  }
}