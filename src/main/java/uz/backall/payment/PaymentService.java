package uz.backall.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;

  public Boolean getPayed(String email, String monthYear) {
    return paymentRepository.findByEmailAndMonthYear(email, monthYear).isPresent();
  }
}