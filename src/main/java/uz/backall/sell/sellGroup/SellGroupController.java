package uz.backall.sell.sellGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store/sell/group")
@RequiredArgsConstructor
public class SellGroupController {
  private final SellGroupService service;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/create")
  public ResponseEntity<SellGroupResponseDTO> create(
    @RequestBody SellGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      service.create(dto)
    );
  }
}