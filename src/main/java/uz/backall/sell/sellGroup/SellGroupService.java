package uz.backall.sell.sellGroup;

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
public class SellGroupService {
  private final SellGroupRepository repository;
  private final StoreRepository storeRepository;

  public SellGroupResponseDTO create(SellGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    SellGroupEntity sellGroup = new SellGroupEntity();
    sellGroup.setAmount(dto.getAmount());
    sellGroup.setCreatedDate(dto.getCreatedDate());
    sellGroup.setStoreId(dto.getStoreId());
    repository.save(sellGroup);

    System.out.println(sellGroup);

    return mapToDTO(sellGroup);
  }

  public Page<SellGroupResponseDTO> getInfo(Long storeId, int page, int size) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellGroupEntity> byStoreProductStoreId = repository.findByStoreId(storeId, pageable);

    List<SellGroupResponseDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private SellGroupResponseDTO mapToDTO(SellGroupEntity sellHistoryEntity) {
    SellGroupResponseDTO responseDTO = new SellGroupResponseDTO();
    responseDTO.setId(sellHistoryEntity.getId());
    responseDTO.setAmount(sellHistoryEntity.getAmount());
    responseDTO.setCreatedDate(sellHistoryEntity.getCreatedDate());
    responseDTO.setStoreId(sellHistoryEntity.getStoreId());
    return responseDTO;
  }
}