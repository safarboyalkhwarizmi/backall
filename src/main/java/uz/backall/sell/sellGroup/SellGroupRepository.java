package uz.backall.sell.sellGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellGroupRepository extends JpaRepository<SellGroupEntity, Long> {

}