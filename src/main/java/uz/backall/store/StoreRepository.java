package uz.backall.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
  List<StoreEntity> getByUserId(Long userId);
//  List<StoreEntity> getByUser_Email(String email);
}