package uz.backall.profit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.backall.profit.profitGroup.ProfitGroupCreateDTO;
import uz.backall.profit.profitGroup.ProfitGroupResponseDTO;
import uz.backall.profit.profitGroup.ProfitGroupService;
import uz.backall.profit.profitHistory.ProfitHistoryCreateDTO;
import uz.backall.profit.profitHistory.ProfitHistoryInfoDTO;
import uz.backall.profit.profitHistory.ProfitHistoryResponseDTO;
import uz.backall.profit.profitHistory.ProfitHistoryService;
import uz.backall.profit.profitHistoryGroup.ProfitHistoryGroupCreateDTO;
import uz.backall.profit.profitHistoryGroup.ProfitHistoryGroupResponseDTO;
import uz.backall.profit.profitHistoryGroup.ProfitHistoryGroupService;

@RestController
@RequestMapping("/api/v1/store/profit")
@RequiredArgsConstructor
public class ProfitController {
  private final ProfitGroupService profitGroupService;
  private final ProfitHistoryService profitHistoryService;
  private final ProfitHistoryGroupService profitHistoryGroupService;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/group/create")
  public ResponseEntity<ProfitGroupResponseDTO> createSellGroup(
    @RequestBody ProfitGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/history/create")
  public ResponseEntity<ProfitHistoryResponseDTO> createSellHistory(
    @RequestBody ProfitHistoryCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitHistoryService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/link/create")
  public ResponseEntity<ProfitHistoryGroupResponseDTO> createSellHistory(
    @RequestBody ProfitHistoryGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.create(dto)
    );
  }

  @GetMapping("/group/get")
  public ResponseEntity<Page<ProfitGroupResponseDTO>> getInfoSellGroup(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfo(storeId, page, size)
    );
  }

  @GetMapping("/history/get")
  public ResponseEntity<Page<ProfitHistoryInfoDTO>> getInfoSellHistory(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryService.getInfo(storeId, page, size)
    );
  }

  @GetMapping("/link/info")
  public ResponseEntity<Page<ProfitHistoryGroupResponseDTO>> getInfoSellHistoryGroup(
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getInfo(storeId, page, size)
    );
  }
}