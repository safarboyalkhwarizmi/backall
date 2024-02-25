package uz.backall.sell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.backall.sell.sellGroup.SellGroupCreateDTO;
import uz.backall.sell.sellGroup.SellGroupResponseDTO;
import uz.backall.sell.sellGroup.SellGroupService;
import uz.backall.sell.sellHistory.SellHistoryCreateDTO;
import uz.backall.sell.sellHistory.SellHistoryInfoDTO;
import uz.backall.sell.sellHistory.SellHistoryResponseDTO;
import uz.backall.sell.sellHistory.SellHistoryService;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupService;

@RestController
@RequestMapping("/api/v1/store/sell")
@RequiredArgsConstructor
public class SellController {
  private final SellGroupService sellGroupService;
  private final SellHistoryService sellHistoryService;
  private final SellHistoryGroupService sellHistoryGroupService;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/group/create")
  public ResponseEntity<SellGroupResponseDTO> createSellGroup(
    @RequestBody SellGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      sellGroupService.create(dto)
    );
  }

  @GetMapping("/group/get")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfoSellGroup(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellGroupService.getInfo(storeId, page, size)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/history/create")
  public ResponseEntity<SellHistoryResponseDTO> createSellHistory(
    @RequestBody SellHistoryCreateDTO dto
  ) {
    return ResponseEntity.ok(
      sellHistoryService.create(dto)
    );
  }

  @GetMapping("/history/get")
  public ResponseEntity<Page<SellHistoryInfoDTO>> getInfoSellHistory(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellHistoryService.getInfo(storeId, page, size)
    );
  }


}
