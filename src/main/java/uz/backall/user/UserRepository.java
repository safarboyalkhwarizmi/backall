package uz.backall.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(Object email);
  Optional<User> findByEmailAndPinCode(String email, String pinCode);

  Optional<User> findById(Long id);
}