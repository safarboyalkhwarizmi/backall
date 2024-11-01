package uz.backall.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.user.UserEntity;

@RestController
@RequestMapping("/api/v1/store/product")
@RequiredArgsConstructor
public class StoreProductController {
  private final StoreProductService service;

  /* FOR CREATING AND UPDATING PRODUCT */
  @PostMapping("/create")
  public ResponseEntity<StoreProductResponseDTO> create(
    @RequestBody StoreProductCreateDTO dto
  ) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping("/get/info")
  public ResponseEntity<Page<StoreProductResponseDTO>> getGlobalInfo(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getInfo(
        storeId,
        page,
        size,
        getUser()
      )
    );
  }

  @GetMapping("/get/info/not/downloaded")
  public ResponseEntity<Page<StoreProductResponseDTO>> getGlobalInfoNotDownloaded(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getInfoNotDownloaded(
        storeId,
        page,
        size,
        getUser()
      )
    );
  }

  private UserEntity getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (UserEntity) authentication.getPrincipal();
  }
}