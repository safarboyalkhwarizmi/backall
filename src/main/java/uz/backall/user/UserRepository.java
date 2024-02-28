package uz.backall.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndPinCode(String email, String pinCode);
  List<User> findByEmailAndPassword(String email, String password);
  Optional<User> findByEmailAndRole(String email, Role role);
}