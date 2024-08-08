package uz.backall.profit.profitAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.profit.profitGroup.ProfitGroupEntity;

@Repository
public interface ProfitAmountDateRepository extends JpaRepository<ProfitAmountDateEntity, Long> {
  Page<ProfitAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<ProfitAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  ProfitAmountDateEntity findTop1ByStoreIdOrderByIdDesc(Long storeId);
}