package uz.backall.sell.sellHistory;

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
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;
import uz.backall.store.product.StoreProductEntity;
import uz.backall.store.product.StoreProductRepository;
import uz.backall.user.Role;
import uz.backall.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellHistoryService {
  private final ProductRepository productRepository;
  private final StoreProductRepository storeProductRepository;
  private final SellHistoryRepository repository;
  private final StoreRepository storeRepository;

  @Transactional
  public SellHistoryResponseDTO create(SellHistoryCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    if (dto == null || dto.getProductId() == null) {
      throw new IllegalArgumentException("Invalid SellHistoryCreateDTO object: " + dto);
    }

    ProductEntity product = productRepository.findById(dto.getProductId())
      .orElseThrow(() -> new ProductNotFoundException("Product with ID " + dto.getProductId() + " not found."));

    Optional<StoreProductEntity> byProductIdAndStoreId = storeProductRepository.findByProductIdAndStoreId(
      dto.getStoreId(), dto.getProductId()
    );

    if (byProductIdAndStoreId.isEmpty()) {
      throw new ProductNotFoundException("Product with ID " + dto.getProductId() + " not found.");
    }

    // STORE PRODUCT COUNT UPDATE
    StoreProductEntity storeProductEntity = byProductIdAndStoreId.get();
    if (
      (storeProductEntity.getCount() - dto.getCount()) >= 0
    ) {
      storeProductEntity.setCount(0.0);
    } else {
      storeProductEntity.setCount(storeProductEntity.getCount() - dto.getCount());
    }

    storeProductEntity.setIsOwnerDownloaded(false);
    storeProductRepository.save(storeProductEntity);

    SellHistoryEntity sellHistory = new SellHistoryEntity();
    sellHistory.setProductId(dto.getProductId());
    sellHistory.setCount(dto.getCount());
    sellHistory.setCountType(dto.getCountType());
    sellHistory.setCreatedDate(dto.getCreatedDate());
    sellHistory.setSellingPrice(dto.getSellingPrice());
    sellHistory.setStoreId(dto.getStoreId());

    repository.save(sellHistory);
    return getSellHistoryResponseDTO(sellHistory);
  }

  private static SellHistoryResponseDTO getSellHistoryResponseDTO(SellHistoryEntity sellHistory) {
    SellHistoryResponseDTO sellHistoryResponseDTO = new SellHistoryResponseDTO();
    sellHistoryResponseDTO.setId(sellHistory.getId());
    sellHistoryResponseDTO.setSellingPrice(sellHistory.getSellingPrice());
    sellHistoryResponseDTO.setCreatedDate(sellHistory.getCreatedDate());
    sellHistoryResponseDTO.setCount(sellHistory.getCount());
    sellHistoryResponseDTO.setCountType(sellHistoryResponseDTO.getCountType());
    sellHistoryResponseDTO.setProductId(sellHistory.getProductId());
    sellHistoryResponseDTO.setStoreId(sellHistory.getStoreId());
    return sellHistoryResponseDTO;
  }

  public Page<SellHistoryInfoDTO> getInfo(Long storeId, int page, int size, User user) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellHistoryEntity> byStoreProductStoreId = repository.findByStoreId(storeId, pageable);

    List<SellHistoryInfoDTO> dtoList;

    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(sellHistoryEntity -> {
          sellHistoryEntity.setIsOwnerDownloaded(true);
          repository.save(sellHistoryEntity);

          return mapToDTO(sellHistoryEntity);
        })
        .collect(Collectors.toList());
    } else {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public Page<SellHistoryInfoDTO> getInfoNotDownloaded(
    Long storeId, int page, int size, User user
  ) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellHistoryEntity> byStoreProductStoreId =
      repository.findByStoreIdAndIsOwnerDownloadedFalse(storeId, pageable);

    List<SellHistoryInfoDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(sellHistoryEntity -> {
        sellHistoryEntity.setIsOwnerDownloaded(true);
        repository.save(sellHistoryEntity);

        return mapToDTO(sellHistoryEntity);
      })
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private SellHistoryInfoDTO mapToDTO(SellHistoryEntity entity) {
    SellHistoryInfoDTO dto = new SellHistoryInfoDTO();
    dto.setId(entity.getId());
    dto.setProductId(entity.getProduct().getId());
    dto.setCount(entity.getCount());
    dto.setSellingPrice(entity.getSellingPrice());
    dto.setCountType(entity.getCountType());
    dto.setCreatedDate(entity.getCreatedDate());
    return dto;
  }
}