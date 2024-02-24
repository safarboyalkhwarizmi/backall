package uz.backall.brands;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {
  private final BrandService service;

  @PostMapping("/create")
  public ResponseEntity<BrandResponseDTO> create(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam String name
  ) {
    return ResponseEntity.ok(service.create(storeId, name));
  }

  @GetMapping("/get/global")
  public ResponseEntity<Page<BrandResponseDTO>> getAll(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(service.getGlobalPage(page, size));
  }

  @GetMapping("/get/local")
  public ResponseEntity<Page<BrandResponseDTO>> getAll(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(service.getLocalPage(storeId, page, size));
  }
}