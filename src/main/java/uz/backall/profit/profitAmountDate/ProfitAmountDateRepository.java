package uz.backall.profit.profitAmountDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.backall.profit.profitGroup.ProfitGroupEntity;
import uz.backall.sell.sellAmountDate.SellAmountDateEntity;

import java.util.Optional;

@Repository
public interface ProfitAmountDateRepository extends JpaRepository<ProfitAmountDateEntity, Long> {
  Page<ProfitAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<ProfitAmountDateEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);

  Optional<ProfitAmountDateEntity> findTop1ByStoreIdOrderByIdDesc(Long storeId);

  Optional<ProfitAmountDateEntity> findByStoreIdAndDate(Long storeId, String date);

  @Query("SELECT SUM(s.amount) FROM ProfitAmountDateEntity s WHERE s.storeId = :storeId AND s.date LIKE :datePattern")
  Long findTotalAmountByStoreIdAndDatePattern(@Param("storeId") Long storeId, @Param("datePattern") String datePattern);

}