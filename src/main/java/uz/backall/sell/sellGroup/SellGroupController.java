package uz.backall.sell.sellGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.backall.sell.sellHistory.SellHistoryInfoDTO;

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

  @GetMapping("/get")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfo(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getInfo(storeId, page, size)
    );
  }
}