package uz.backall.sell.sellAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellAmountDateRepository extends JpaRepository<SellAmountDateEntity, Long> {
  Page<SellAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<SellAmountDateEntity> findTop1ByStoreIdOrderByIdDesc(Long storeId);

  Optional<SellAmountDateEntity> findByStoreIdAndDate(Long storeId, String date);

  @Query("SELECT SUM(s.amount) FROM SellAmountDateEntity s WHERE s.storeId = :storeId AND s.date LIKE :datePattern")
  Long findTotalAmountByStoreIdAndDatePattern(@Param("storeId") Long storeId, @Param("datePattern") String datePattern);
}