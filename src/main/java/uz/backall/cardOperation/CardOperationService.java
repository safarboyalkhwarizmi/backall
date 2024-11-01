package uz.backall.cardOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.cardOperation.card.CardEntity;
import uz.backall.cardOperation.card.CardRepository;
import uz.backall.cardOperation.cardToken.CardTokenEntity;
import uz.backall.cardOperation.cardToken.CardTokenRepository;
import uz.backall.cardOperation.cardUser.CardUserEntity;
import uz.backall.cardOperation.cardUser.CardUserRepository;
import uz.backall.cardOperation.card.CardCreateRequestDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardOperationService {
  private final CardRepository cardRepository;
  private final CardUserRepository cardUserRepository;
  private final CardTokenRepository cardTokenRepository;

  @Transactional
  public Long createCard(CardCreateRequestDTO dto, Long userId) {
    CardEntity card = new CardEntity();
    card.setNumber(dto.getNumber());
    card.setExpire(dto.getExpire());
    cardRepository.save(card);

    CardUserEntity cardUser = new CardUserEntity();
    cardUser.setCardId(card.getId());
    cardUser.setUserId(userId);
    cardUserRepository.save(cardUser);
    return card.getId();
  }

  @Transactional
  public Boolean createCardToken(Long cardId, String token) {
    CardTokenEntity cardTokenEntity = new CardTokenEntity();
    cardTokenEntity.setToken(token);
    cardTokenEntity.setCardId(cardId);
    cardTokenRepository.save(cardTokenEntity);

    return true;
  }
}
