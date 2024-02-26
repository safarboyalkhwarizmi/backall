package uz.backall.profit.profitGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitGroupRepository extends JpaRepository<ProfitGroupEntity, Long> {

  Page<ProfitGroupEntity> findByStoreId(Long storeId, Pageable pageable);
}