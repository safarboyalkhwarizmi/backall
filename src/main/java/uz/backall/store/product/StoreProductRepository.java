package uz.backall.store.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
    List<StoreProductEntity> findByStoreId(Long storeId);

    List<StoreProductEntity> findByStoreIdAndProductId(Long storeId, Long productId);
    List<StoreProductEntity> findByStoreIdAndProductName(Long storeId, String productName);

    Optional<StoreProductEntity> findByProductIdAndStoreIdAndCreatedDateAndExpiredDate(Long productId, Long storeId, LocalDate createdDate, LocalDate expiredDate);
}