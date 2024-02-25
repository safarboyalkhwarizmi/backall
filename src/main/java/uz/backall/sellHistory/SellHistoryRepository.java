package uz.backall.sellHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SellHistoryRepository extends JpaRepository<SellHistoryEntity, Long> {
  Page<SellHistoryEntity> findByStoreId(Long storeId, Pageable pageable);
}