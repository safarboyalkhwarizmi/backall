package uz.backall.sell.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store/sell/history")
@RequiredArgsConstructor
public class SellHistoryController {
  private final SellHistoryService service;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/create")
  public ResponseEntity<SellHistoryResponseDTO> create(
    @RequestBody SellHistoryCreateDTO dto
  ) {
    return ResponseEntity.ok(
      service.create(dto)
    );
  }

  @GetMapping("/get")
  public ResponseEntity<Page<SellHistoryInfoDTO>> getInfo(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      service.getInfo(storeId, page, size)
    );
  }
}