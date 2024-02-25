package uz.backall.sell.sellGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellGroupService {
  private final SellGroupRepository sellGroupRepository;

  public SellGroupResponseDTO create(SellGroupCreateDTO dto) {
    SellGroupEntity sellGroup = new SellGroupEntity();
    sellGroup.setAmount(dto.getAmount());
    sellGroup.setCreatedDate(dto.getCreatedDate());
    sellGroup.setStoreId(dto.getStoreId());
    sellGroupRepository.save(sellGroup);

    SellGroupResponseDTO responseDTO = new SellGroupResponseDTO();
    responseDTO.setAmount(dto.getAmount());
    responseDTO.setCreatedDate(dto.getCreatedDate());
    responseDTO.setStoreId(dto.getStoreId());
    return responseDTO;
  }
}