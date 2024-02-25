package uz.backall.sell.sellGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellHistory.SellHistoryEntity;

@Repository
public interface SellGroupRepository extends JpaRepository<SellGroupEntity, Long> {

  Page<SellGroupEntity> findByStoreId(Long storeId, Pageable pageable);
}