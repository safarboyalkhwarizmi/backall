package uz.backall.brands.localStoreBrand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalStoreBrandRepository extends JpaRepository<LocalStoreBrandEntity, Long> {
  Optional<LocalStoreBrandEntity> findByBrand_NameAndStoreId(String name, Long storeId);
  Page<LocalStoreBrandEntity> findByStoreId(Long storeId, Pageable pageable);
}