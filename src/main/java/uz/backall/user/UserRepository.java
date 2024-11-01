package uz.backall.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmailAndPinCode(String email, String pinCode);
  List<UserEntity> findByEmailAndPassword(String email, String password);
  List<UserEntity> findByEmail(String email);
  Optional<UserEntity> findByEmailAndRole(String email, Role role);
}