package uz.backall.sell.sellHistoryGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellHistoryGroupRepository extends JpaRepository<SellHistoryGroupEntity, Long> {
}