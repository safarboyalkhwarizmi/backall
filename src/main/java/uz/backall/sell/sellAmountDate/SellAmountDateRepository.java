package uz.backall.sell.sellAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupEntity;

@Repository
public interface SellAmountDateRepository extends JpaRepository<SellAmountDateEntity, Long> {
  Page<SellAmountDateEntity> findByIdLessThanAndStoreId(Long id, Long storeId, Pageable pageable);

  Page<SellAmountDateEntity> findByIdLessThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long storeId, Pageable pageable);

  SellAmountDateEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}