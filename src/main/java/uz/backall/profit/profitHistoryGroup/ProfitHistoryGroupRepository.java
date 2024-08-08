package uz.backall.profit.profitHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellAmountDate.SellAmountDateEntity;

@Repository
public interface ProfitHistoryGroupRepository extends JpaRepository<ProfitHistoryGroupEntity, Long> {
  Page<ProfitHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<ProfitHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  ProfitHistoryGroupEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}