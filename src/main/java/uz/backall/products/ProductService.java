package uz.backall.products;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.brands.BrandEntity;
import uz.backall.brands.BrandRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public Boolean create(ProductCreateDTO dto) {
        Optional<BrandEntity> byId = brandRepository.findById(dto.getBrandId());
        if (byId.isEmpty()) {
            return false;
        }

        Optional<ProductEntity> bySerialNumber = productRepository.findBySerialNumber(dto.getSerialNumber());
        if (bySerialNumber.isPresent()) {
            return false;
        }

        ProductEntity product = new ProductEntity();
        product.setBrandId(dto.getBrandId());
        product.setName(dto.getName());
        product.setSerialNumber(dto.getSerialNumber());
        productRepository.save(product);
        return true;
    }
}
