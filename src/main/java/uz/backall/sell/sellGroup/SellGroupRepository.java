package uz.backall.sell.sellGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellGroupRepository extends JpaRepository<SellGroupEntity, Long> {

  Page<SellGroupEntity> findByStoreId(Long storeId, Pageable pageable);

  Page<SellGroupEntity> findByStoreIdAndIsOwnerDownloadedFalse(Long storeId, Pageable pageable);

}