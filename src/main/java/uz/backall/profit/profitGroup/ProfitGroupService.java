package uz.backall.profit.profitGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitGroupService {
  private final ProfitGroupRepository repository;
  private final StoreRepository storeRepository;

  public ProfitGroupResponseDTO create(ProfitGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    ProfitGroupEntity profitGroup = new ProfitGroupEntity();
    profitGroup.setProfit(dto.getProfit());
    profitGroup.setCreatedDate(dto.getCreatedDate());
    profitGroup.setStoreId(dto.getStoreId());
    repository.save(profitGroup);

    return mapToDTO(profitGroup);
  }

  public Page<ProfitGroupResponseDTO> getInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitGroupEntity> byStoreProductStoreId = repository.findByStoreId(storeId, pageable);

    List<ProfitGroupResponseDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private ProfitGroupResponseDTO mapToDTO(ProfitGroupEntity profitHistoryEntity) {
    ProfitGroupResponseDTO responseDTO = new ProfitGroupResponseDTO();
    responseDTO.setId(profitHistoryEntity.getId());
    responseDTO.setProfit(profitHistoryEntity.getProfit());
    responseDTO.setCreatedDate(profitHistoryEntity.getCreatedDate());
    responseDTO.setStoreId(profitHistoryEntity.getStoreId());
    return responseDTO;
  }
}