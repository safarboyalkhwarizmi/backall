package uz.backall.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.cardOperation.CardOperationService;
import uz.backall.cardOperation.card.CardCreateRequestDTO;
import uz.backall.cardOperation.card.CodeIsWrongException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static uz.backall.util.PaymeUtil.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final CardOperationService cardOperationService;

  private final String authToken = "5e730e8e0b852a417aa49ceb";

  public Boolean getPayed(String email, String monthYear) {
    return paymentRepository.findByEmailAndMonthYear(email, monthYear).isPresent();
  }

  public PaymentMakeResponseDTO make(CardCreateRequestDTO dto, Long userId) {
    // SAVING
    Long cardId = cardOperationService.createCard(dto, userId);

    // TODO CREATE CARD REQUEST FROM PAYME
    //...
    String tokenFromCreateCardRequest =
      createCard(dto.getNumber(), dto.getExpire(), cardId, authToken);
    cardOperationService.createCardToken(cardId, tokenFromCreateCardRequest);

    String phoneNumberFromVerifyCodeRequest = getVerifyCode(tokenFromCreateCardRequest, cardId, authToken);
    PaymentMakeResponseDTO response = new PaymentMakeResponseDTO();
    response.setToken(tokenFromCreateCardRequest);
    response.setPhone(phoneNumberFromVerifyCodeRequest);
    return response;
  }

  public Boolean verify(PaymentVerifyRequestDTO dto, String email) {
    // TODO MAKE PAYMENT WITH THAT TOKEN
    //....
    Boolean isCodeAndTokenValid = verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
    if (isCodeAndTokenValid.equals(false)) {
      throw new CodeIsWrongException("Code is wrong");
    }

    String productId =
      createReceipt(500000, 12333, "Наименование услуги или товара", 250000, 2, "02001001005034001", 12, "1397132");

    payReceipt(productId, dto.getToken());

    // SAVE PAYMENT DATA
    PaymentEntity payment = new PaymentEntity();
    payment.setEmail(email);

    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
    String formattedDate = currentDate.format(formatter);
    payment.setMonthYear(formattedDate);

    paymentRepository.save(payment);

    return true;
  }
}