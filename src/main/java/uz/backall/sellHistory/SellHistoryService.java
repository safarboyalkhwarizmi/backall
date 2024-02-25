package uz.backall.sellHistory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductNotFoundException;
import uz.backall.products.ProductRepository;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellHistoryService {
  private final ProductRepository productRepository;
  private final SellHistoryRepository repository;

  @Transactional
  public boolean create(List<SellHistoryCreateDTO> dtoList) {
    if (dtoList == null || dtoList.isEmpty()) {
      throw new IllegalArgumentException("Sell history list is null or empty.");
    }

    if (dtoList.size() > 20) {
      throw new IllegalArgumentException("Exceeds maximum allowed sell history creations in a single request.");
    }

    // Validate SellHistoryCreateDTO objects
    for (SellHistoryCreateDTO dto : dtoList) {
      if (dto == null || dto.getProductId() == null || dto.getCount() <= 0 || dto.getSellingPrice() <= 0) {
        throw new IllegalArgumentException("Invalid SellHistoryCreateDTO object: " + dto);
      }
    }

    // Fetch all product IDs
    Set<Long> productIds = dtoList.stream()
      .map(SellHistoryCreateDTO::getProductId)
      .collect(Collectors.toSet());
    Map<Long, ProductEntity> productsMap = productRepository.findAllById(productIds)
      .stream()
      .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

    // Create SellHistoryEntity objects and save them
    List<SellHistoryEntity> sellHistories = new ArrayList<>();
    for (SellHistoryCreateDTO dto : dtoList) {
      ProductEntity product = productsMap.get(dto.getProductId());
      if (product == null) {
        throw new ProductNotFoundException("Product with ID " + dto.getProductId() + " not found.");
      }

      SellHistoryEntity sellHistory = new SellHistoryEntity();
      sellHistory.setProduct(product);
      sellHistory.setCount(dto.getCount());
      sellHistory.setCountType(dto.getCountType());
      sellHistory.setCreatedDate(dto.getCreatedDate());
      sellHistory.setSellingPrice(dto.getSellingPrice());

      sellHistories.add(sellHistory);
    }

    repository.saveAll(sellHistories);

    return true;
  }

  public Page<SellHistoryInfoDTO> getInfo(Long storeId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<SellHistoryEntity> byStoreProductStoreId = repository.findByStoreId(storeId, pageable);

    List<SellHistoryInfoDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private SellHistoryInfoDTO mapToDTO(SellHistoryEntity entity) {
    SellHistoryInfoDTO dto = new SellHistoryInfoDTO();
    dto.setId(entity.getId());
    dto.setStoreProductId(entity.getProduct().getId());
    dto.setCount(entity.getCount());
    dto.setSellingPrice(entity.getSellingPrice());
    dto.setCountType(entity.getCountType());
    dto.setCreatedDate(entity.getCreatedDate());
    return dto;
  }


}