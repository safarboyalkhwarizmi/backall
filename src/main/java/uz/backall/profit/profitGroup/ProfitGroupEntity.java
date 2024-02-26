
package uz.backall.profit.profitGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.store.StoreEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profit_group")
public class ProfitGroupEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime createdDate;
  private Double profit;

  @Column(name = "store_id")
  private Long storeId;

  @ManyToOne
  @JoinColumn(name = "store_id", insertable = false, updatable = false)
  private StoreEntity store;
}