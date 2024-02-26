package uz.backall.profit.profitHistory;

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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitHistoryService {
  private final ProductRepository productRepository;
  private final ProfitHistoryRepository repository;
  private final StoreRepository storeRepository;

  @Transactional
  public ProfitHistoryResponseDTO create(ProfitHistoryCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    if (dto == null || dto.getProductId() == null || dto.getCount() <= 0 || dto.getProfit() <= 0) {
      throw new IllegalArgumentException("Invalid profitHistoryCreateDTO object: " + dto);
    }

    ProductEntity product = productRepository.findById(dto.getProductId())
      .orElseThrow(() -> new ProductNotFoundException("Product with ID " + dto.getProductId() + " not found."));

    ProfitHistoryEntity profitHistory = new ProfitHistoryEntity();
    profitHistory.setProductId(dto.getProductId());
    profitHistory.setCount(dto.getCount());
    profitHistory.setCountType(dto.getCountType());
    profitHistory.setCreatedDate(dto.getCreatedDate());
    profitHistory.setProfit(dto.getProfit());
    profitHistory.setStoreId(dto.getStoreId());

    repository.save(profitHistory);
    ProfitHistoryResponseDTO profitHistoryResponseDTO = getprofitHistoryResponseDTO(profitHistory);
    return profitHistoryResponseDTO;
  }

  private static ProfitHistoryResponseDTO getprofitHistoryResponseDTO(ProfitHistoryEntity profitHistory) {
    ProfitHistoryResponseDTO profitHistoryResponseDTO = new ProfitHistoryResponseDTO();
    profitHistoryResponseDTO.setId(profitHistory.getId());
    profitHistoryResponseDTO.setProfit(profitHistory.getProfit());
    profitHistoryResponseDTO.setCreatedDate(profitHistory.getCreatedDate());
    profitHistoryResponseDTO.setCount(profitHistory.getCount());
    profitHistoryResponseDTO.setCountType(profitHistoryResponseDTO.getCountType());
    profitHistoryResponseDTO.setProductId(profitHistory.getProductId());
    profitHistoryResponseDTO.setStoreId(profitHistory.getStoreId());
    return profitHistoryResponseDTO;
  }

  public Page<ProfitHistoryInfoDTO> getInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitHistoryEntity> byStoreProductStoreId = repository.findByStoreId(storeId, pageable);

    List<ProfitHistoryInfoDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private ProfitHistoryInfoDTO mapToDTO(ProfitHistoryEntity entity) {
    ProfitHistoryInfoDTO dto = new ProfitHistoryInfoDTO();
    dto.setId(entity.getId());
    dto.setStoreProductId(entity.getProduct().getId());
    dto.setCount(entity.getCount());
    dto.setProfit(entity.getProfit());
    dto.setCountType(entity.getCountType());
    dto.setCreatedDate(entity.getCreatedDate());
    return dto;
  }
}