package uz.backall.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.user.UserEntity;

@Slf4j
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
    log.info("/product/create, Request: {}", dto);
    return ResponseEntity.ok(
      service.create(dto)
    );
  }

  @GetMapping("/get/global/info")
  public ResponseEntity<Page<ProductResponseDTO>> getGlobalInfo(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getGlobalProductsInfo(page, size)
    );
  }

  @GetMapping("/get/local/info")
  public ResponseEntity<Page<ProductResponseDTO>> getLocalInfo(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getLocalProductsInfo(
        storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/get/local/info/not/downloaded")
  public ResponseEntity<Page<ProductResponseDTO>> getLocalInfoNotDownloadedOnlyForBoss(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getLocalProductsNotDownloaded(
        storeId, page, size, getUser()
      )
    );
  }

  private UserEntity getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (UserEntity) authentication.getPrincipal();
  }
}