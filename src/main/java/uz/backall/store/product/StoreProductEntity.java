package uz.backall.store.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.products.ProductEntity;
import uz.backall.store.StoreEntity;

@Getter
@Setter
@Entity
@Table(name = "store_product")
public class StoreProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id")
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    private StoreEntity store;

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity product;
    private Boolean nds;
    private Long price;
    private Long sellingPrice;
    private Integer percentage;
}