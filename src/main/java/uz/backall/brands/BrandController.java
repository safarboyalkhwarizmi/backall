package uz.backall.brands;

import lombok.RequiredArgsConstructor;
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

  @GetMapping("/get/all")
  public ResponseEntity<List<BrandResponseDTO>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }
}