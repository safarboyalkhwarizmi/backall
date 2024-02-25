package uz.backall.sell.sellHistoryGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.sell.sellGroup.SellGroupEntity;
import uz.backall.sell.sellGroup.SellGroupNotFoundException;
import uz.backall.sell.sellGroup.SellGroupRepository;
import uz.backall.sell.sellHistory.SellHistoryEntity;
import uz.backall.sell.sellHistory.SellHistoryNotFoundException;
import uz.backall.sell.sellHistory.SellHistoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellHistoryGroupService {
  private final SellHistoryGroupRepository sellHistoryGroupRepository;
  private final SellGroupRepository sellGroupRepository;
  private final SellHistoryRepository sellHistoryRepository;

  public Boolean create(SellHistoryGroupCreateDTO dto) {
    Optional<SellGroupEntity> byGroupId = sellGroupRepository.findById(dto.getSellGroupId());
    if (byGroupId.isEmpty()) {
      throw new SellGroupNotFoundException("Sell group not found");
    }

    Optional<SellHistoryEntity> byHistoryId = sellHistoryRepository.findById(dto.getSellHistoryId());
    if (byHistoryId.isEmpty()) {
      throw new SellHistoryNotFoundException("Sell history not found");
    }

    SellHistoryGroupEntity sellHistoryGroup = new SellHistoryGroupEntity();
    sellHistoryGroup.setSellGroupId(dto.getSellGroupId());
    sellHistoryGroup.setSellHistoryId(dto.getSellHistoryId());
    sellHistoryGroupRepository.save(sellHistoryGroup);

    return true;
  }

  public Page<SellHistoryGroupResponseDTO> getInfo(Long storeId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<SellHistoryGroupEntity> byStoreId = sellHistoryGroupRepository.findByStoreId(storeId, pageable);

    List<SellHistoryGroupResponseDTO> dtoList =
      byStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }

  private SellHistoryGroupResponseDTO mapToDTO(SellHistoryGroupEntity sellHistoryGroupEntity) {
    SellHistoryGroupResponseDTO sellHistoryGroupResponseDTO = new SellHistoryGroupResponseDTO();
    sellHistoryGroupResponseDTO.setSellGroupId(sellHistoryGroupEntity.getSellGroupId());
    sellHistoryGroupResponseDTO.setSellHistoryId(sellHistoryGroupResponseDTO.getSellHistoryId());
    return sellHistoryGroupResponseDTO;
  }
}