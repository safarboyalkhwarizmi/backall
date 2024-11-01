package uz.backall.cardOperation.cardToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTokenRepository extends JpaRepository<CardTokenEntity, Long> {
}
