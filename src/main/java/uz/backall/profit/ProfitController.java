package uz.backall.profit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.profit.profitAmountDate.ProfitAmountDateCreateDTO;
import uz.backall.profit.profitAmountDate.ProfitAmountDateRepository;
import uz.backall.profit.profitAmountDate.ProfitAmountDateResponse;
import uz.backall.profit.profitAmountDate.ProfitAmountDateService;
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
import uz.backall.sell.sellAmountDate.SellAmountDateResponse;
import uz.backall.user.User;

@RestController
@RequestMapping("/api/v1/store/profit")
@RequiredArgsConstructor
public class ProfitController {
  private final ProfitGroupService profitGroupService;
  private final ProfitHistoryService profitHistoryService;
  private final ProfitHistoryGroupService profitHistoryGroupService;
  private final ProfitAmountDateService profitAmountDateService;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/group/create")
  public ResponseEntity<ProfitGroupResponseDTO> createProfitGroup(
    @RequestBody ProfitGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/history/create")
  public ResponseEntity<ProfitHistoryResponseDTO> createProfitHistory(
    @RequestBody ProfitHistoryCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitHistoryService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/link/create")
  public ResponseEntity<ProfitHistoryGroupResponseDTO> createProfitHistory(
    @RequestBody ProfitHistoryGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/amount/date/create")
  public ResponseEntity<ProfitAmountDateResponse> createProfitHistory(
    @RequestBody ProfitAmountDateCreateDTO dto
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.create(dto)
    );
  }

  @GetMapping("/month/amount")
  public ResponseEntity<Long> getThisMonthAmountInfo(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.getMonthAmount(
        storeId
      )
    );
  }

  @GetMapping("/amount/date/get")
  public ResponseEntity<Page<ProfitAmountDateResponse>> getAmountDateInfo(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/amount/date/get/by")
  public ResponseEntity<ProfitAmountDateResponse> getSellAmountDateInfoByDate(
    @RequestParam(value = "date") String date,
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.getInfoByDate(
        date, storeId
      )
    );
  }


  @GetMapping("/amount/date/lastId")
  public ResponseEntity<Long> getLastIdAmountDate(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.getLastId(storeId)
    );
  }

  @GetMapping("/amount/date/get/not/downloaded")
  public ResponseEntity<Page<ProfitAmountDateResponse>> getAmountDateInfoNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitAmountDateService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/get")
  public ResponseEntity<Page<ProfitGroupResponseDTO>> getInfoProfitGroup(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/lastId")
  public ResponseEntity<Long> getLastIdProfitGroup(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitGroupService.getLastId(storeId)
    );
  }

  @GetMapping("/group/get/not/downloaded")
  public ResponseEntity<Page<ProfitGroupResponseDTO>> getInfoProfitGroupNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/history/get")
  public ResponseEntity<Page<ProfitHistoryInfoDTO>> getInfoProfitHistory(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/history/lastId")
  public ResponseEntity<Long> getLastIdProfitHistory(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitHistoryService.getLastId(storeId)
    );
  }

  @GetMapping("/history/get/not/downloaded")
  public ResponseEntity<Page<ProfitHistoryInfoDTO>> getInfoProfitHistoryNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/link/info")
  public ResponseEntity<Page<ProfitHistoryGroupResponseDTO>> getInfoProfitHistoryGroup(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/link/get/lastId")
  public ResponseEntity<Long> getLastIdProfitHistoryGroup(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getLastId(storeId)
    );
  }

  @GetMapping("/link/info/not/downloaded")
  public ResponseEntity<Page<ProfitHistoryGroupResponseDTO>> getInfoProfitHistoryGroupNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (User) authentication.getPrincipal();
  }
}