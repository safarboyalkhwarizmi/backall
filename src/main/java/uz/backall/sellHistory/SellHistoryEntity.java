package uz.backall.sellHistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.products.ProductEntity;
import uz.backall.store.product.StoreProductEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sell_history")
public class SellHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Double count;

  @Column
  private Double sellingPrice;

  @Column
  private String countType;

  @Column
  private LocalDateTime createdDate;
  
  @Column(name = "product_id")
  private Long productId;

  @ManyToOne
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  private ProductEntity product;
}