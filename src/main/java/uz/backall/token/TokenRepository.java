package uz.backall.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  @Query(value = """
    select t from Token t inner join UserEntity u\s
    on t.userEntity.id = u.id\s
    where u.id = :id and (t.expired = false or t.revoked = false)\s
    """)
  List<Token> findAllValidTokenByUser(Long id);

  Optional<Token> findByToken(String token);
}
