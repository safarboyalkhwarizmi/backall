package uz.backall.products;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.brands.*;
import uz.backall.brands.localStoreBrand.LocalStoreBrandEntity;
import uz.backall.brands.localStoreBrand.LocalStoreBrandRepository;
import uz.backall.products.localStoreProduct.LocalStoreProductEntity;
import uz.backall.products.localStoreProduct.LocalStoreProductRepository;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;
import uz.backall.user.Role;
import uz.backall.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final LocalStoreProductRepository localStoreProductRepository;
  private final StoreRepository storeRepository;
  private final LocalStoreBrandRepository localStoreBrandRepository;

  public ProductResponseDTO create(ProductCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Optional<BrandEntity> byBrandName = brandRepository.findByName(dto.getBrandName());
    BrandEntity brand;

    if (byBrandName.isEmpty()) {
      brand = new BrandEntity();
      brand.setName(dto.getBrandName());
      brand.setBrandType(BrandType.LOCAL);
      brandRepository.save(brand);

      LocalStoreBrandEntity localStoreBrandEntity = new LocalStoreBrandEntity();
      localStoreBrandEntity.setBrandId(brand.getId());
      localStoreBrandEntity.setStoreId(dto.getStoreId());
      localStoreBrandRepository.save(localStoreBrandEntity);
    } else {
      brand = byBrandName.get();
    }


    List<LocalStoreProductEntity> byProductSerialNumber = localStoreProductRepository.findByProduct_SerialNumber(dto.getSerialNumber());
    if (byProductSerialNumber.size() > 1) {
      throw new SerialAlreadyExistException("Serial already exist.");
    }

    ProductEntity product = new ProductEntity();
    product.setBrandId(brand.getId());
    product.setName(dto.getName());
    product.setSerialNumber(dto.getSerialNumber());
    product.setType(dto.getType());
    product = productRepository.save(product);

    if (dto.getType() == ProductType.LOCAL) {
      // THE PRODUCTS CREATED BY STORE
      LocalStoreProductEntity localStoreProduct = new LocalStoreProductEntity();
      localStoreProduct.setProductId(product.getId()); // it will be null or not??
      localStoreProduct.setStoreId(dto.getStoreId());
      localStoreProductRepository.save(localStoreProduct);
    }

    return new ProductResponseDTO(
      product.getId(),
      product.getName(),
      product.getSerialNumber(),
      brand.getName()
    );
  }

  public Page<ProductResponseDTO> getGlobalProductsInfo(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<ProductEntity> productPage = productRepository.findByType(ProductType.GLOBAL, pageable);

    Page<ProductResponseDTO> responsePage = productPage.map(productEntity ->
      new ProductResponseDTO(
        productEntity.getId(),
        productEntity.getSerialNumber(),
        productEntity.getName(),
        productEntity.getBrand().getName()
      )
    );

    return responsePage;
  }

  public Page<ProductResponseDTO> getLocalProductsInfo(Long storeId, int page, int size, UserEntity userEntity) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<LocalStoreProductEntity> localProductPage = localStoreProductRepository.findByStoreId(storeId, pageable);

    Page<ProductEntity> productPage = localProductPage.map(LocalStoreProductEntity::getProduct);


    return productPage.map(productEntity -> {
        if (userEntity.getRole().equals(Role.BOSS)) {
          productEntity.setIsOwnerDownloaded(true);
          productRepository.save(productEntity);
        }

        return new ProductResponseDTO(
          productEntity.getId(),
          productEntity.getSerialNumber(),
          productEntity.getName(),
          productEntity.getBrand().getName()
        );
      }
    );
  }

  public Page<ProductResponseDTO> getLocalProductsNotDownloaded(
    Long storeId,
    int page,
    int size,
    UserEntity userEntity
  ) {
    if (!userEntity.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<LocalStoreProductEntity> localProductPage =
      localStoreProductRepository.findByStoreIdAndProductIsOwnerDownloadedFalse(storeId, pageable);

    Page<ProductResponseDTO> responsePage = localProductPage.map(localStoreProductEntity -> {
      ProductEntity productEntity = localStoreProductEntity.getProduct();
      productEntity.setIsOwnerDownloaded(true);
      productRepository.save(productEntity);

      return new ProductResponseDTO(
        productEntity.getId(),
        productEntity.getSerialNumber(),
        productEntity.getName(),
        productEntity.getBrand().getName()
      );
    });

    return responsePage;
  }
}