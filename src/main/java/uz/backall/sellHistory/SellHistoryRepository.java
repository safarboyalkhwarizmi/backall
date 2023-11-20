package uz.backall.sellHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SellHistoryRepository extends JpaRepository<SellHistoryEntity, Long> {
  List<SellHistoryEntity> findByStoreProductStoreId(Long storeId);
}