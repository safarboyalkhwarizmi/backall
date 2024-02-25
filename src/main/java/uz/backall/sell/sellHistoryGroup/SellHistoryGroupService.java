package uz.backall.sell.sellHistoryGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.sell.sellGroup.SellGroupEntity;
import uz.backall.sell.sellGroup.SellGroupNotFoundException;
import uz.backall.sell.sellGroup.SellGroupRepository;
import uz.backall.sell.sellHistory.SellHistoryEntity;
import uz.backall.sell.sellHistory.SellHistoryNotFoundException;
import uz.backall.sell.sellHistory.SellHistoryRepository;

import java.util.Optional;

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
}