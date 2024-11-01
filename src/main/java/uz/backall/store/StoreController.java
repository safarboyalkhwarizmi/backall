package uz.backall.store;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.user.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {
  private final StoreService service;

  @GetMapping("/get/stores")
  public ResponseEntity<List<StoreResponseDTO>> getStores() {
    return ResponseEntity.ok(service.getStoresByUserId(getUserId()));
  }

  private Long getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserEntity userEntity = (UserEntity) authentication.getPrincipal();

    return userEntity.getId();
  }
}
