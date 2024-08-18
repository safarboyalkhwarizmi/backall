package uz.backall.profit.profitHistoryGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellAmountDate.SellAmountDateEntity;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfitHistoryGroupRepository extends JpaRepository<ProfitHistoryGroupEntity, Long> {
  Page<ProfitHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<ProfitHistoryGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<ProfitHistoryGroupEntity> findTop1ByStoreIdOrderByIdDesc(Long storeId);

  List<ProfitHistoryGroupEntity> findByStoreIdAndProfitGroupIdGreaterThanEqual(Long storeId, Long groupId);
  List<ProfitHistoryGroupEntity> findByStoreIdAndProfitGroupId(Long storeId, Long groupId);
}