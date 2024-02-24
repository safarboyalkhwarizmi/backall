package uz.backall.products;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService service;

  @PostMapping("/create")
  public ResponseEntity<Boolean> create(
    @RequestBody ProductCreateDTO dto
  ) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping("/get/global/info")
  public ResponseEntity<Page<ProductResponseDTO>> getGlobalInfo(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(service.getGlobalProductsInfo(page, size));
  }

  @GetMapping("/get/local/info")
  public ResponseEntity<Page<ProductResponseDTO>> getLocalInfo(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(service.getLocalProductsInfo(storeId, page, size));
  }
}