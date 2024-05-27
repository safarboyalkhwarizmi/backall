package uz.backall.store.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
  List<StoreProductEntity> findByStoreId(Long storeId);

  List<StoreProductEntity> findByStoreIdAndProductId(Long storeId, Long productId);

  List<StoreProductEntity> findByStoreIdAndProductName(Long storeId, String productName);

    /* TODO FOR FIRST OCTOBER 2023 SUNDAY
    Optional<StoreProductEntity> findByProductIdAndStoreIdAndCreatedDateAndExpiredDate(Long productId, Long storeId, LocalDate createdDate, LocalDate expiredDate);
    */

  Optional<StoreProductEntity> findByProductIdAndStoreId(Long productId, Long storeId);

  Page<StoreProductEntity> findByStoreId(Long storeId, Pageable pageable);

  Page<StoreProductEntity> findByIsOwnerDownloadedFalse(Pageable pageable);
}