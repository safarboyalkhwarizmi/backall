package uz.backall.cardOperation.cardUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.cardOperation.card.CardEntity;
import uz.backall.user.UserEntity;

@Getter
@Setter
@Entity
@Table(name = "card_user")
public class CardUserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "card_id")
  private Long cardId;

  @ManyToOne
  @JoinColumn(name = "card_id", insertable = false, updatable = false)
  private CardEntity card;

  @Column(name = "user_id")
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private UserEntity userEntity;
}