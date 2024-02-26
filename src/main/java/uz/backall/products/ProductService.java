package uz.backall.products;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.brands.BrandEntity;
import uz.backall.brands.BrandRepository;
import uz.backall.products.localStoreProduct.LocalStoreProductEntity;
import uz.backall.products.localStoreProduct.LocalStoreProductRepository;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final LocalStoreProductRepository localStoreProductRepository;
  private final StoreRepository storeRepository;

  public Boolean create(ProductCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Optional<BrandEntity> byId = brandRepository.findById(dto.getBrandId());
    if (byId.isEmpty()) {
      return false;
    }

    Optional<ProductEntity> bySerialNumber = productRepository.findBySerialNumber(dto.getSerialNumber());
    if (bySerialNumber.isPresent()) {
      return false;
    }

    ProductEntity product = new ProductEntity();
    product.setBrandId(dto.getBrandId());
    product.setName(dto.getName());
    product.setSerialNumber(dto.getSerialNumber());
    product.setType(dto.getType());
    productRepository.save(product);

    if (dto.getType() == ProductType.LOCAL) {
      // THE PRODUCTS CREATED BY STORE
      LocalStoreProductEntity localStoreProduct = new LocalStoreProductEntity();
      localStoreProduct.setProductId(product.getId()); // it will be null or not??
      localStoreProduct.setStoreId(dto.getStoreId());
      localStoreProductRepository.save(localStoreProduct);
    }
    return true;
  }

  public Page<ProductResponseDTO> getGlobalProductsInfo(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<ProductEntity> productPage = productRepository.findByType(ProductType.GLOBAL, pageable);

    Page<ProductResponseDTO> responsePage = productPage.map(productEntity ->
      new ProductResponseDTO(
        productEntity.getId(),
        productEntity.getSerialNumber(),
        productEntity.getName(),
        productEntity.getBrandId()
      )
    );

    return responsePage;
  }

  public Page<ProductResponseDTO> getLocalProductsInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<LocalStoreProductEntity> localProductPage = localStoreProductRepository.findByStoreId(storeId, pageable);

    Page<ProductEntity> productPage = localProductPage.map(LocalStoreProductEntity::getProduct);

    Page<ProductResponseDTO> responsePage = productPage.map(productEntity ->
      new ProductResponseDTO(
        productEntity.getId(),
        productEntity.getSerialNumber(),
        productEntity.getName(),
        productEntity.getBrandId()
      )
    );

    return responsePage;
  }
}
