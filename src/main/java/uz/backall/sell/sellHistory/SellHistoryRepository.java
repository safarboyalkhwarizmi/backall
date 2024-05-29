package uz.backall.sell.sellHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SellHistoryRepository extends JpaRepository<SellHistoryEntity, Long> {
  Page<SellHistoryEntity> findByStoreId(Long storeId, Pageable pageable);
  Page<SellHistoryEntity> findByStoreIdAndIsOwnerDownloadedFalse(Long storeId, Pageable pageable);
}