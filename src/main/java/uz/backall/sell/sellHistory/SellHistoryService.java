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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellHistoryService {
  private final ProductRepository productRepository;
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

    SellHistoryEntity sellHistory = new SellHistoryEntity();
    sellHistory.setProductId(dto.getProductId());
    sellHistory.setCount(dto.getCount());
    sellHistory.setCountType(dto.getCountType());
    sellHistory.setCreatedDate(dto.getCreatedDate());
    sellHistory.setSellingPrice(dto.getSellingPrice());
    sellHistory.setStoreId(dto.getStoreId());

    repository.save(sellHistory);
    SellHistoryResponseDTO sellHistoryResponseDTO = getSellHistoryResponseDTO(sellHistory);
    return sellHistoryResponseDTO;
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

  public Page<SellHistoryInfoDTO> getInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

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
    dto.setProductId(entity.getProduct().getId());
    dto.setCount(entity.getCount());
    dto.setSellingPrice(entity.getSellingPrice());
    dto.setCountType(entity.getCountType());
    dto.setCreatedDate(entity.getCreatedDate());
    return dto;
  }
}