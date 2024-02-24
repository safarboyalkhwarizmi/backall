package uz.backall.brands.localStoreBrand;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.brands.BrandEntity;
import uz.backall.store.StoreEntity;

@Getter
@Setter
@Entity
public class LocalStoreBrandEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "store_id")
  private Long storeId;

  @ManyToOne
  @JoinColumn(name = "store_id", insertable = false, updatable = false)
  private StoreEntity store;

  @Column(name = "brand_id")
  private Long brandId;

  @ManyToOne
  @JoinColumn(name = "brand_id", insertable = false, updatable = false)
  private BrandEntity brand;
}