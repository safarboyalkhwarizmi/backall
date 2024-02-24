package uz.backall.products;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.brands.BrandEntity;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String serialNumber;

  @Column
  private String name;

  @Column(name = "brand_id")
  private Long brandId;

  @ManyToOne
  @JoinColumn(name = "brand_id", insertable = false, updatable = false)
  private BrandEntity brand;

  @Column
  @Enumerated(EnumType.STRING)
  private ProductType type;
}