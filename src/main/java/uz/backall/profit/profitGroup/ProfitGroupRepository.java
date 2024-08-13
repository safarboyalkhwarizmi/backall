package uz.backall.profit.profitGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.profit.profitHistory.ProfitHistoryEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProfitGroupRepository extends JpaRepository<ProfitGroupEntity, Long> {

  Page<ProfitGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);
  Page<ProfitGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Page<ProfitGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndCreatedDateBetween(
    Long id, Long id2, Long storeId,
    LocalDateTime fromLocalDate, LocalDateTime toLocalDate,
    Pageable pageable
  );

  Page<ProfitGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalseAndCreatedDateBetween(
    Long id, Long id2, Long storeId,
    LocalDateTime fromLocalDate, LocalDateTime toLocalDate,
    Pageable pageable
  );
  Optional<ProfitGroupEntity> findTop1ByStoreIdOrderByIdDesc(Long storeId);
}