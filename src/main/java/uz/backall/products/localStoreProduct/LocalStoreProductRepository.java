package uz.backall.products.localStoreProduct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalStoreProductRepository extends JpaRepository<LocalStoreProductEntity, Long> {
  Page<LocalStoreProductEntity> findByStoreId(Long storeId, Pageable pageable);
}