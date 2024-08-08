package uz.backall.sell.sellHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellHistory.SellHistoryEntity;

@Repository
public interface SellHistoryGroupRepository extends JpaRepository<SellHistoryGroupEntity, Long> {
  Page<SellHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  SellHistoryGroupEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}