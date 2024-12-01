package uz.backall.cardOperation.card;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CardCreateRequestDTO {
  private String number;
  private String expire;
}