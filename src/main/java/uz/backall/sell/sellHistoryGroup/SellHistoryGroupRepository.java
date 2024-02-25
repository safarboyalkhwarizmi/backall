package uz.backall.sell.sellHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellHistoryGroupRepository extends JpaRepository<SellHistoryGroupEntity, Long> {
  Page<SellHistoryGroupEntity> findByStoreId(Long storeId, Pageable pageable);
}