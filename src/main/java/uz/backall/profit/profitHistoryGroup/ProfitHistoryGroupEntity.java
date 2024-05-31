package uz.backall.profit.profitHistoryGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.profit.profitGroup.ProfitGroupEntity;
import uz.backall.profit.profitHistory.ProfitHistoryEntity;
import uz.backall.store.StoreEntity;

@Getter
@Setter
@Entity
@Table(name = "profit_history_group")
public class ProfitHistoryGroupEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "profit_group_id")
  private Long profitGroupId;

  @ManyToOne
  @JoinColumn(name = "profit_group_id", insertable = false, updatable = false)
  private ProfitGroupEntity profitGroup;

  @Column(name = "profit_history_id")
  private Long profitHistoryId;

  @ManyToOne
  @JoinColumn(name = "profit_history_id", insertable = false, updatable = false)
  private ProfitHistoryEntity profitHistory;

  @Column(name = "store_id")
  private Long storeId;

  @ManyToOne
  @JoinColumn(name = "store_id", insertable = false, updatable = false)
  private StoreEntity store;


  @Column
  private Boolean isOwnerDownloaded = false;
}