package uz.backall.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store/product")
@RequiredArgsConstructor
public class StoreProductController {
    private final StoreProductService service;

    @PostMapping("/create")
    public ResponseEntity<Boolean> create(
            @RequestBody List<StoreProductCreateDTO> dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/get/info")
    public ResponseEntity<List<StoreProductInfoDTO>> getInfo(
            @RequestParam Long storeId
    ) {
        return ResponseEntity.ok(service.getInfo(storeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreProductInfoDTO>> searchInfo(
            @RequestParam Long storeId,
            @RequestParam String productName
    ) {
        return ResponseEntity.ok(service.getInfoByName(storeId, productName));
    }

}
