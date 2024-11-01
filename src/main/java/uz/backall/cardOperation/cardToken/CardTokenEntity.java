package uz.backall.cardOperation.cardToken;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.cardOperation.card.CardEntity;

@Getter
@Setter
@Entity
@Table(name = "card_token")
public class CardTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition="text", length=10485760)
  private String token;

  @Column(name = "card_id")
  private Long cardId;

  @ManyToOne
  @JoinColumn(name = "card_id", insertable = false, updatable = false)
  private CardEntity card;
}
