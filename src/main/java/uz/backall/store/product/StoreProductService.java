package uz.backall.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductNotFoundException;
import uz.backall.products.ProductRepository;
import uz.backall.sell.sellHistory.SellingPriceException;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;
import uz.backall.user.Role;
import uz.backall.user.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreProductService {
  private final StoreProductRepository storeProductRepository;
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;

  public StoreProductResponseDTO create(StoreProductCreateDTO dto) {
    Optional<ProductEntity> byProductId = productRepository.findById(dto.getProductId());
    Optional<StoreEntity> byStoreId = storeRepository.findById(dto.getStoreId());

    Optional<StoreProductEntity> byProductIdInStore =
      storeProductRepository.findByProductIdAndStoreId(
        dto.getProductId(),
        dto.getStoreId()
      );

    if (byProductId.isPresent() && byStoreId.isPresent()) { // PRODUCT AND STORE EXIST
      StoreProductEntity storeProduct;
      // PRODUCT NOT EXIST IN STORE
      if (byProductIdInStore.isEmpty()) {
        storeProduct = getStoreProductEntity(dto);
      }
      // PRODUCT ALREADY EXIST IN STORE UPDATE
      else {
        storeProduct = byProductIdInStore.get();

        storeProduct.setNds(dto.getNds());
        storeProduct.setPrice(dto.getPrice());
        storeProduct.setSellingPrice(dto.getSellingPrice());
        storeProduct.setPercentage(dto.getPercentage());
        storeProduct.setCount(dto.getCount());

        if (dto.getPercentage() == null && dto.getSellingPrice() != null) {
          if (dto.getPrice() > dto.getSellingPrice()) {
            throw new SellingPriceException("Price is more than selling price!");
          }

          storeProduct.setSellingPrice(dto.getSellingPrice());
          double percentage = (double) (
            (dto.getSellingPrice() - dto.getPrice()) / dto.getPrice()) * 100;
          storeProduct.setPercentage(percentage);
        } else if (dto.getPercentage() != null && dto.getSellingPrice() == null) {
          storeProduct.setPercentage(dto.getPercentage());
          double sellingPrice = ((dto.getPrice() / 100) * dto.getPercentage()) + dto.getPrice();

          if (dto.getPrice() > sellingPrice) {
            throw new SellingPriceException("Price is more than selling price!");
          }
          storeProduct.setSellingPrice(sellingPrice);
        } else {
          throw new SellingPriceException("Something must be null!");
        }
      }

      storeProductRepository.save(storeProduct);

      return new StoreProductResponseDTO(
        storeProduct.getId(),
        storeProduct.getProductId(),
        storeProduct.getNds(),
        storeProduct.getPrice(),
        storeProduct.getSellingPrice(),
        storeProduct.getPercentage(),
        storeProduct.getCount(),
        storeProduct.getCountType()
      );
    }

    throw new ProductNotFoundException("Product not exist.");
  }

  private StoreProductEntity getStoreProductEntity(StoreProductCreateDTO dto) {
    StoreProductEntity storeProduct = new StoreProductEntity();
    storeProduct.setStoreId(dto.getStoreId());
    storeProduct.setProductId(dto.getProductId());
    storeProduct.setNds(dto.getNds());

    storeProduct.setPrice(dto.getPrice());

    if (dto.getPercentage() == null && dto.getSellingPrice() != null) {
      if (dto.getPrice() > dto.getSellingPrice()) {
        throw new SellingPriceException("Price is more than selling price!");
      }

      storeProduct.setSellingPrice(dto.getSellingPrice());
      double percentage = (double) (
        (dto.getSellingPrice() - dto.getPrice()) / dto.getPrice()) * 100;
      storeProduct.setPercentage(percentage);
    } else if (dto.getPercentage() != null && dto.getSellingPrice() == null) {
      storeProduct.setPercentage(dto.getPercentage());
      double sellingPrice = ((dto.getPrice() / 100) * dto.getPercentage()) + dto.getPrice();

      if (dto.getPrice() > sellingPrice) {
        throw new SellingPriceException("Price is more than selling price!");
      }
      storeProduct.setSellingPrice(sellingPrice);
    } else {
      throw new SellingPriceException("Something must be null!");
    }

    storeProduct.setPrice(dto.getPrice());
    storeProduct.setSellingPrice(dto.getSellingPrice());
    storeProduct.setPercentage(dto.getPercentage());

    storeProduct.setCountType(dto.getCountType());
    storeProduct.setCount(dto.getCount());

        /* TODO FOR FIRST OCTOBER 2023 SUNDAY
        storeProduct.setCreatedDate(dto.getCreatedDate());
        storeProduct.setExpiredDate(dto.getExpiredDate());
        */

    return storeProduct;
  }

  public Page<StoreProductResponseDTO> getInfo(
    Long storeId, Integer page, Integer size, User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<StoreProductEntity> productPage = storeProductRepository.findAll(pageable);

    return productPage.map(productEntity -> {
        if (user.getRole().equals(Role.BOSS)) {
          productEntity.setIsOwnerDownloaded(true);
          storeProductRepository.save(productEntity);
        }

        return new StoreProductResponseDTO(
          productEntity.getId(),
          productEntity.getProductId(),
          productEntity.getNds(),
          productEntity.getPrice(),
          productEntity.getSellingPrice(),
          productEntity.getPercentage(),
          productEntity.getCount(),
          productEntity.getCountType()
        );
      }
    );
  }

  public Page<StoreProductResponseDTO> getInfoNotDownloaded(
    Long storeId, Integer page, Integer size, User user
  ) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<StoreProductEntity> productPage = storeProductRepository.findByIsOwnerDownloadedFalse(pageable);

    return productPage.map(productEntity -> {
        productEntity.setIsOwnerDownloaded(true);
        storeProductRepository.save(productEntity);

        return new StoreProductResponseDTO(
          productEntity.getId(),
          productEntity.getProductId(),
          productEntity.getNds(),
          productEntity.getPrice(),
          productEntity.getSellingPrice(),
          productEntity.getPercentage(),
          productEntity.getCount(),
          productEntity.getCountType()
        );
      }
    );
  }
}