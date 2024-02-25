package uz.backall.sell.sellGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellGroupService {
  private final SellGroupRepository repository;

  public SellGroupResponseDTO create(SellGroupCreateDTO dto) {
    SellGroupEntity sellGroup = new SellGroupEntity();
    sellGroup.setAmount(dto.getAmount());
    sellGroup.setCreatedDate(dto.getCreatedDate());
    sellGroup.setStoreId(dto.getStoreId());
    repository.save(sellGroup);

    return mapToDTO(sellGroup);
  }

  public Page<SellGroupResponseDTO> getInfo(Long storeId, int page, int size) {
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