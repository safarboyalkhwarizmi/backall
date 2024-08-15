package uz.backall.profit.profitHistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.products.ProductEntity;
import uz.backall.store.StoreEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profit_history")
public class ProfitHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long count;

  private Long profit;

  private String countType;

  private LocalDateTime createdDate;
  
  @Column(name = "product_id")
  private Long productId;

  @ManyToOne
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  private ProductEntity product;

  @Column(name = "store_id")
  private Long storeId;

  @ManyToOne
  @JoinColumn(name = "store_id", insertable = false, updatable = false)
  private StoreEntity store;


  @Column
  private Boolean isOwnerDownloaded = false;
}