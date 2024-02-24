package uz.backall.brands;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
  Optional<BrandEntity> findByName(String name);

  Page<BrandEntity> findByBrandType(BrandType brandType, Pageable pageable);
}