package uz.backall.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.sellGroup.SellGroupEntity;
import uz.backall.sellGroup.SellGroupRepository;
import uz.backall.sellHistoryGroup.SellHistoryGroupEntity;
import uz.backall.sellHistoryGroup.SellHistoryGroupRepository;
import uz.backall.store.product.StoreProductEntity;
import uz.backall.store.product.StoreProductInfoDTO;
import uz.backall.store.product.StoreProductRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellHistoryService {
  private final SellHistoryRepository repository;
  private final StoreProductRepository storeProductRepository;
  private final SellGroupRepository sellGroupRepository;
  private final SellHistoryGroupRepository sellHistoryGroupRepository;

  public Boolean create(List<SellHistoryCreateDTO> dtoList) {
    SellGroupEntity sellGroup = new SellGroupEntity();
    sellGroup = sellGroupRepository.save(sellGroup);

    for (SellHistoryCreateDTO dto : dtoList) {
      List<StoreProductEntity> byStoreIdAndProductId = storeProductRepository.findByStoreIdAndProductId(dto.getStoreId(), dto.getProductId());
      if (!byStoreIdAndProductId.isEmpty()) {
        StoreProductEntity storeProduct = byStoreIdAndProductId.get(0);
        SellHistoryEntity sellHistory = new SellHistoryEntity();
        sellHistory.setStoreProductId(storeProduct.getId());
        sellHistory.setCount(dto.getCount());
        sellHistory.setCreatedDate(LocalDateTime.now());
        storeProduct.setCount(storeProduct.getCount() - dto.getCount());

        sellHistory = repository.save(sellHistory);
        storeProductRepository.save(storeProduct);

        SellHistoryGroupEntity sellHistoryGroup = new SellHistoryGroupEntity();
        sellHistoryGroup.setSellGroupId(sellHistoryGroup.getId());
        sellHistoryGroup.setSellHistoryId(sellHistory.getId());
        sellHistoryGroupRepository.save(sellHistoryGroup);
      }
    }

    return true;
  }

  public List<SellHistoryInfoDTO> getInfo(Long storeId, String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Parse the input date string into a LocalDate
    LocalDate localDate = LocalDate.parse(date, dateFormatter);

    List<SellHistoryEntity> byStoreId = repository.findByStoreProductStoreId(storeId);

    List<SellHistoryInfoDTO> result = new LinkedList<>();
    for (SellHistoryEntity storeProduct : byStoreId) {
      SellHistoryInfoDTO info = new SellHistoryInfoDTO();
      info.setName(storeProduct.getStoreProduct().getProduct().getName());
      info.setCount(storeProduct.getStoreProduct().getCount());
      info.setTime(Time.valueOf(storeProduct.getCreatedDate().toLocalTime()));

      result.add(info);
    }

    return result;
  }
}