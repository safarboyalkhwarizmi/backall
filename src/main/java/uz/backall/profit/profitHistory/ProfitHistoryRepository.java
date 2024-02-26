package uz.backall.profit.profitHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.backall.sell.sellHistory.SellHistoryEntity;


public interface ProfitHistoryRepository extends JpaRepository<ProfitHistoryEntity, Long> {
  Page<ProfitHistoryEntity> findByStoreId(Long storeId, Pageable pageable);
}