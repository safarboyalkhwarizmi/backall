package uz.backall.sell.sellGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellGroupRepository extends JpaRepository<SellGroupEntity, Long> {
  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreId(Long id, Long id2, Long storeId, Pageable pageable);

  Page<SellGroupEntity> findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(Long id, Long id2, Long storeId, Pageable pageable);


  SellGroupEntity findTop1ByStoreIdOrderByCreatedDateDesc(Long storeId);
}
