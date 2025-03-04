package uz.backall.sell.sellGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backall.profit.profitGroup.ProfitGroupEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SellGroupRepository extends JpaRepository<SellGroupEntity, Long> {
  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<SellGroupEntity> findTop1ByStoreIdOrderByCreatedDateDesc(Long storeId);

  Optional<SellGroupEntity> findTop1ByStoreIdAndCreatedDateBetweenOrderByCreatedDateDesc(
    Long storeId, LocalDateTime toLocalDate, LocalDateTime fromLocalDate
  );

  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndCreatedDateBetween(
    Long id, Long id2, Long storeId,
    LocalDateTime toLocalDate, LocalDateTime fromLocalDate,
    Pageable pageable
  );

  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalseAndCreatedDateBetween(
    Long id, Long id2, Long storeId,
    LocalDateTime toLocalDate, LocalDateTime fromLocalDate,
    Pageable pageable
  );

  Optional<SellGroupEntity> findByIdAndStoreId(Long id, Long storeId);
}
