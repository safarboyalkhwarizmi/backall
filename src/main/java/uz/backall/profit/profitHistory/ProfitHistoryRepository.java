package uz.backall.profit.profitHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.backall.profit.profitHistoryGroup.ProfitHistoryGroupEntity;

public interface ProfitHistoryRepository extends JpaRepository<ProfitHistoryEntity, Long> {
  Page<ProfitHistoryEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);
  Page<ProfitHistoryEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  ProfitHistoryEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}