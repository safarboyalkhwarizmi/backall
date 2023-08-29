package uz.backall.store.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProductEntity, Long> {
    @Query("select distinct productId from StoreProductEntity where storeId =?1 ")
    List<StoreProductEntity> findByStoreId(Long storeId);

    Long countByProductId(Long productId);
}