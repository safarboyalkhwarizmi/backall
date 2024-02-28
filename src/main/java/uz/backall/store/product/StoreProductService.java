package uz.backall.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductRepository;
import uz.backall.sell.sellHistory.SellingPriceException;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreProductService {
  private final StoreProductRepository repository;
  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;

  public Boolean create(StoreProductCreateDTO dto) {
    Optional<ProductEntity> byProductId = productRepository.findById(dto.getProductId());
    Optional<StoreEntity> byStoreId = storeRepository.findById(dto.getStoreId());

    Optional<StoreProductEntity> byProductIdInStore = repository.findByProductIdAndStoreId(
      dto.getProductId(),
      dto.getStoreId()
    );

    if (byProductId.isPresent() && byStoreId.isPresent()) {
      StoreProductEntity storeProduct;
      if (byProductIdInStore.isEmpty()) {
        storeProduct = getStoreProductEntity(dto);
      } else {
        storeProduct = byProductIdInStore.get();
        storeProduct.setNds(dto.getNds());
        storeProduct.setPrice(dto.getPrice());
        storeProduct.setSellingPrice(dto.getSellingPrice());
        storeProduct.setPercentage(dto.getPercentage());
        storeProduct.setCount(dto.getCount());
      }
      repository.save(storeProduct);
    }

    return true;
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
    if (dto.getCountType().equals(CountType.NUM) || dto.getCountType().equals(CountType.GR)) {
      Integer i = dto.getCount().intValue();
      storeProduct.setCount(i.doubleValue());
    } else {
      storeProduct.setCount(dto.getCount());
    }

        /* TODO FOR FIRST OCTOBER 2023 SUNDAY
        storeProduct.setCreatedDate(dto.getCreatedDate());
        storeProduct.setExpiredDate(dto.getExpiredDate());
        */

    return storeProduct;
  }

  public Page<StoreProductResponseDTO> getInfo(Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<StoreProductEntity> productPage = repository.findAll(pageable);

    Page<StoreProductResponseDTO> responsePage = productPage.map(productEntity ->
      new StoreProductResponseDTO(
        productEntity.getId(),
        productEntity.getProductId(),
        productEntity.getNds(),
        productEntity.getPrice(),
        productEntity.getSellingPrice(),
        productEntity.getPercentage(),
        productEntity.getCount(),
        productEntity.getCountType()
      )
    );

    return responsePage;
  }
}