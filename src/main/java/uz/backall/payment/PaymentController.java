package uz.backall.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.cardOperation.card.CardCreateRequestDTO;
import uz.backall.user.UserEntity;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @GetMapping("/get")
  public ResponseEntity<Boolean> getPayed(
    @RequestParam String email,
    @RequestParam String monthYear
  ) {
    return ResponseEntity.ok(paymentService.getPayed(email, monthYear));
  }

  @PostMapping("/make")
  public ResponseEntity<String> make(
    @RequestBody CardCreateRequestDTO dto
  ) {
    return ResponseEntity.ok(paymentService.make(dto, getUser().getId()));
  }

  @PostMapping("/verify")
  public ResponseEntity<Boolean> make(
    @RequestParam String token,
    @RequestParam String code
  ) {
    return ResponseEntity.ok(paymentService.verify(token, code, getUser().getEmail()));
  }

  private UserEntity getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity userEntity = (UserEntity) authentication.getPrincipal();

    return userEntity;
  }
}