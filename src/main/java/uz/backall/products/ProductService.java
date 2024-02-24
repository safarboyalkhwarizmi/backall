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

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final LocalStoreProductRepository localStoreProductRepository;

  public Boolean create(ProductCreateDTO dto) {
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
    // Create a Pageable object with the given page number and page size
    Pageable pageable = PageRequest.of(page, size);

    // Retrieve the paginated list of ProductEntity objects using the repository
    Page<ProductEntity> productPage = productRepository.findByType(ProductType.GLOBAL, pageable);

    // Map ProductEntity objects to ProductResponseDTO objects
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
    // Create a Pageable object with the given page number and page size
    Pageable pageable = PageRequest.of(page, size);

    // Retrieve the paginated list of LocalStoreProductEntity objects using the repository
    Page<LocalStoreProductEntity> localProductPage = localStoreProductRepository.findByStoreId(storeId, pageable);

    // Extract the list of ProductEntity objects from LocalStoreProductEntity objects
    Page<ProductEntity> productPage = localProductPage.map(LocalStoreProductEntity::getProduct);

    // Map ProductEntity objects to ProductResponseDTO objects
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
