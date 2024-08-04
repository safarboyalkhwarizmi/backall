package uz.backall.profit.profitHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellAmountDate.SellAmountDateEntity;

@Repository
public interface ProfitHistoryGroupRepository extends JpaRepository<ProfitHistoryGroupEntity, Long> {
  Page<ProfitHistoryGroupEntity> findByIdLessThanAndStoreId(Long id, Long storeId, Pageable pageable);
  Page<ProfitHistoryGroupEntity> findByIdLessThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long storeId, Pageable pageable);

  ProfitHistoryGroupEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);

}