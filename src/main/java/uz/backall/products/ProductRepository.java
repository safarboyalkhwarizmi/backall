package uz.backall.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  Optional<ProductEntity> findBySerialNumber(String serialNumber);

  Page<ProductEntity> findByType(ProductType type, Pageable pageable);
}