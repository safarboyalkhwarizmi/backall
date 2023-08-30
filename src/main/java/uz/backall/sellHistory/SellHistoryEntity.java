package uz.backall.sellHistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @Column(name = "store_product_id")
    private Long storeProductId;

    @ManyToOne
    @JoinColumn(name = "store_product_id", insertable = false, updatable = false)
    private StoreProductEntity storeProduct;

    private Double count;

    private LocalDateTime createdDate;
}