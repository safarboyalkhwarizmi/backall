package uz.backall.sell.sellHistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.products.ProductEntity;
import uz.backall.store.StoreEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sell_history")
public class SellHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double count;

  private Double sellingPrice;

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
}