package uz.backall.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.store.product.StoreProductEntity;
import uz.backall.store.product.StoreProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellHistoryService {
    private final SellHistoryRepository repository;
    private final StoreProductRepository storeProductRepository;

    public Boolean create(List<SellHistoryCreateDTO> dtoList) {
        for (SellHistoryCreateDTO dto : dtoList) {
            List<StoreProductEntity> byStoreIdAndProductId = storeProductRepository.findByStoreIdAndProductId(dto.getStoreId(), dto.getProductId());
            if (!byStoreIdAndProductId.isEmpty()) {
                StoreProductEntity storeProduct = byStoreIdAndProductId.get(0);
                SellHistoryEntity sellHistory = new SellHistoryEntity();
                sellHistory.setStoreProductId(storeProduct.getId());
                sellHistory.setCount(dto.getCount());
                storeProduct.setSoldCount(storeProduct.getSoldCount() + 1);

                repository.save(sellHistory);
                storeProductRepository.save(storeProduct);
            }
        }

        return true;
    }


    public SellHistoryInfoDTO getInfo() {

        return null;
    }
}