package uz.backall.products.localStoreProduct;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.brands.BrandEntity;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductType;
import uz.backall.store.StoreEntity;

@Getter
@Setter
@Entity
@Table(name = "local_store_product")
public class LocalStoreProductEntity {
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
}