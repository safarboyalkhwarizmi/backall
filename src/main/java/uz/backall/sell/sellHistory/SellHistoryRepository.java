package uz.backall.sell.sellHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.backall.sell.sellGroup.SellGroupEntity;

import java.util.Optional;


public interface SellHistoryRepository extends JpaRepository<SellHistoryEntity, Long> {
  Page<SellHistoryEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellHistoryEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<SellHistoryEntity> findTop1ByStoreIdOrderByCreatedDateDesc(Long storeId);
}