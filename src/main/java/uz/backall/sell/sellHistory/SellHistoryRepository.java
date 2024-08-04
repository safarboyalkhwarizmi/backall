package uz.backall.sell.sellHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.backall.sell.sellGroup.SellGroupEntity;


public interface SellHistoryRepository extends JpaRepository<SellHistoryEntity, Long> {
  Page<SellHistoryEntity> findByIdLessThanAndStoreId(Long id, Long storeId, Pageable pageable);

  Page<SellHistoryEntity> findByIdLessThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long storeId, Pageable pageable);

  SellHistoryEntity findTop1ByStoreIdOrderByCreatedDateDesc(Long storeId);
}