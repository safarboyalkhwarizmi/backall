package uz.backall.sell.sellAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellAmountDateRepository extends JpaRepository<SellAmountDateEntity, Long> {
  Page<SellAmountDateEntity> findByStoreId(Long storeId, Pageable pageable);
  Page<SellAmountDateEntity> findByStoreIdAndIsOwnerDownloadedFalse(Long storeId, Pageable pageable);
}