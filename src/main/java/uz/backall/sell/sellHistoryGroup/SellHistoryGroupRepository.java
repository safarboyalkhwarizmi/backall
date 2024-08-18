package uz.backall.sell.sellHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellHistory.SellHistoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellHistoryGroupRepository extends JpaRepository<SellHistoryGroupEntity, Long> {
  Page<SellHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<SellHistoryGroupEntity> findTop1ByStoreIdOrderByIdDesc(Long storeId);

  List<SellHistoryGroupEntity> findByStoreIdAndSellGroupIdGreaterThanEqual(Long storeId, Long sellGroupId);

  List<SellHistoryGroupEntity> findByStoreIdAndSellGroupId(Long storeId, Long groupId);
}