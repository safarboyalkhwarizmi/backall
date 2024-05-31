package uz.backall.profit.profitAmountDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "profit_amount_date")
public class ProfitAmountDateEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String date;
  private Double amount;
  private Long storeId;

  @Column
  private Boolean isOwnerDownloaded = false;
}
