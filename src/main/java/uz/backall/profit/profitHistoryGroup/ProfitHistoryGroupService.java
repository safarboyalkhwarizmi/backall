package uz.backall.profit.profitHistoryGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.profit.profitGroup.ProfitGroupEntity;
import uz.backall.profit.profitGroup.ProfitGroupNotFoundException;
import uz.backall.profit.profitGroup.ProfitGroupRepository;
import uz.backall.profit.profitHistory.ProfitHistoryEntity;
import uz.backall.profit.profitHistory.ProfitHistoryNotFoundException;
import uz.backall.profit.profitHistory.ProfitHistoryRepository;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitHistoryGroupService {
  private final ProfitHistoryGroupRepository profitHistoryGroupRepository;
  private final ProfitGroupRepository profitGroupRepository;
  private final ProfitHistoryRepository profitHistoryRepository;
  private final StoreRepository storeRepository;

  public ProfitHistoryGroupResponseDTO create(ProfitHistoryGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Optional<ProfitGroupEntity> byGroupId = profitGroupRepository.findById(dto.getProfitGroupId());
    if (byGroupId.isEmpty()) {
      throw new ProfitGroupNotFoundException("Sell group not found");
    }

    Optional<ProfitHistoryEntity> byHistoryId =
      profitHistoryRepository.findById(dto.getProfitHistoryId());
    if (byHistoryId.isEmpty()) {
      throw new ProfitHistoryNotFoundException("Sell history not found");
    }

    ProfitHistoryGroupEntity profitHistoryGroup = new ProfitHistoryGroupEntity();
    profitHistoryGroup.setProfitGroupId(dto.getProfitGroupId());
    profitHistoryGroup.setProfitHistoryId(dto.getProfitHistoryId());
    profitHistoryGroup.setStoreId(dto.getStoreId());
    profitHistoryGroupRepository.save(profitHistoryGroup);

    return new ProfitHistoryGroupResponseDTO(
      profitHistoryGroup.getId(),
      profitHistoryGroup.getProfitGroupId(),
      profitHistoryGroup.getProfitHistoryId()
    );
  }

  public Page<ProfitHistoryGroupResponseDTO> getInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitHistoryGroupEntity> byStoreId = profitHistoryGroupRepository.findByStoreId(storeId, pageable);

    List<ProfitHistoryGroupResponseDTO> dtoList =
      byStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }

  private ProfitHistoryGroupResponseDTO mapToDTO(ProfitHistoryGroupEntity profitHistoryGroupEntity) {
    return new ProfitHistoryGroupResponseDTO(profitHistoryGroupEntity.getId(), profitHistoryGroupEntity.getProfitGroupId(), profitHistoryGroupEntity.getProfitHistoryId());
  }
}