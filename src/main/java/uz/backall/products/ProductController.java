package uz.backall.products;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService service;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/create")
  public ResponseEntity<ProductResponseDTO> create(
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