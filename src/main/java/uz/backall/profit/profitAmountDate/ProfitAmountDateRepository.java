package uz.backall.profit.profitAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitAmountDateRepository extends JpaRepository<ProfitAmountDateEntity, Long> {
  Page<ProfitAmountDateEntity> findByStoreId(Long storeId, Pageable pageable);
}