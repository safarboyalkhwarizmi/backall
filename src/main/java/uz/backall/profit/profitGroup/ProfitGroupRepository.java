package uz.backall.profit.profitGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.profit.profitHistory.ProfitHistoryEntity;

@Repository
public interface ProfitGroupRepository extends JpaRepository<ProfitGroupEntity, Long> {

  Page<ProfitGroupEntity> findByIdLessThanAndStoreId(Long id, Long storeId, Pageable pageable);
  Page<ProfitGroupEntity> findByIdLessThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long storeId, Pageable pageable);

  ProfitGroupEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}