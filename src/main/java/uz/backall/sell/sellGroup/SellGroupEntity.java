package uz.backall.sell.sellGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.backall.store.StoreEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sell_group")
@ToString
public class SellGroupEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime createdDate;
  private Double amount;

  @Column(name = "store_id")
  private Long storeId;

  @ManyToOne
  @JoinColumn(name = "store_id", insertable = false, updatable = false)
  private StoreEntity store;

  @Column
  private Boolean isOwnerDownloaded = false;
}