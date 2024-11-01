package uz.backall.cardOperation.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCreateRequestDTO {
  private String number;
  private String expire;
}