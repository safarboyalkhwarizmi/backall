package uz.backall.brands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public Boolean create(String brandName) {
        Optional<BrandEntity> byName = brandRepository.findByName(brandName);
        if (byName.isEmpty()) {
            BrandEntity brand = new BrandEntity();
            brand.setName(brandName);
            brandRepository.save(brand);
            return true;
        }

        return false;
    }

    public List<BrandResponseDTO> getAll() {
        List<BrandEntity> all = brandRepository.findAll();

        List<BrandResponseDTO> result = new LinkedList<>();
        for (BrandEntity brand : all) {
            all.add(brand);
        }

        return result;
    }
}