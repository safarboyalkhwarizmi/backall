package uz.backall.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}