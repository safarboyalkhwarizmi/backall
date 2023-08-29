package uz.backall.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductRepository;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreProductService {
    private final StoreProductRepository repository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public Boolean create(List<StoreProductCreateDTO> dtoList) {
        for (StoreProductCreateDTO dto : dtoList) {
            Optional<ProductEntity> byProductId = productRepository.findById(dto.getProductId());
            Optional<StoreEntity> byStoreId = storeRepository.findById(dto.getStoreId());

            if (byProductId.isPresent() || byStoreId.isPresent()) {
                StoreProductEntity storeProduct = getStoreProductEntity(dto);
                repository.save(storeProduct);
            }
        }

        return true;
    }

    private StoreProductEntity getStoreProductEntity(StoreProductCreateDTO dto) {
        StoreProductEntity storeProduct = new StoreProductEntity();
        storeProduct.setStoreId(dto.getStoreId());
        storeProduct.setProductId(dto.getProductId());
        storeProduct.setNds(dto.getNds());

        /* TODO WORK ON SETTING PRICES */
        storeProduct.setPrice(dto.getPrice());
        storeProduct.setSellingPrice(dto.getSellingPrice());
        storeProduct.setPercentage(dto.getPercentage());

        storeProduct.setCreatedDate(dto.getCreatedDate());
        storeProduct.setExpiredDate(dto.getExpiredDate());
        return storeProduct;
    }

    public List<StoreProductInfoDTO> getInfo(Long storeId) {

        List<StoreProductEntity> byStoreId = repository.findByStoreId(storeId);

        List<StoreProductInfoDTO> result = new LinkedList<>();
        for (StoreProductEntity storeProduct : byStoreId) {
            StoreProductInfoDTO info = new StoreProductInfoDTO();
            info.setProductId(storeProduct.getProductId());
            info.setName(storeProduct.getProduct().getName());
            info.setProductCount(repository.countByProductId(storeProduct.getProductId()));
            result.add(info);
        }

        return result;
    }
}