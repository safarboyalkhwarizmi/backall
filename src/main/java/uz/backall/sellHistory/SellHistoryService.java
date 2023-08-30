package uz.backall.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.sellGroup.SellGroupEntity;
import uz.backall.sellGroup.SellGroupRepository;
import uz.backall.sellHistoryGroup.SellHistoryGroupEntity;
import uz.backall.sellHistoryGroup.SellHistoryGroupRepository;
import uz.backall.store.product.StoreProductEntity;
import uz.backall.store.product.StoreProductRepository;

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


    public SellHistoryInfoDTO getInfo() {

        return null;
    }
}