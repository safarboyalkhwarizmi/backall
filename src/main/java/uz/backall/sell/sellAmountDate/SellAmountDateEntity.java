package uz.backall.sell.sellAmountDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "sell_amount_date")
public class SellAmountDateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String date;
  private Double amount;
  private Long storeId;
}
