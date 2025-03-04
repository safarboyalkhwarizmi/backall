package uz.backall.profit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.profit.profitAmountDate.ProfitAmountDateCreateDTO;
import uz.backall.profit.profitAmountDate.ProfitAmountDateResponse;
import uz.backall.profit.profitAmountDate.ProfitAmountDateService;
import uz.backall.profit.profitGroup.ProfitGroupCreateDTO;
import uz.backall.profit.profitGroup.ProfitGroupResponseDTO;
import uz.backall.profit.profitGroup.ProfitGroupService;
import uz.backall.profit.profitHistory.ProfitHistoryCreateDTO;
import uz.backall.profit.profitHistory.ProfitHistoryInfoDTO;
import uz.backall.profit.profitHistory.ProfitHistoryResponseDTO;
import uz.backall.profit.profitHistory.ProfitHistoryService;
import uz.backall.profit.profitHistoryGroup.*;
import uz.backall.user.UserEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store/profit")
@RequiredArgsConstructor
@Slf4j
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
    log.info("/profit/group/create, Request: {}", dto);

    return ResponseEntity.ok(
      profitGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/history/create")
  public ResponseEntity<ProfitHistoryResponseDTO> createProfitHistory(
    @RequestBody ProfitHistoryCreateDTO dto
  ) {
    log.info("/profit/history/create, Request: {}", dto);

    return ResponseEntity.ok(
      profitHistoryService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/link/create")
  public ResponseEntity<ProfitHistoryGroupResponseDTO> createProfitHistory(
    @RequestBody ProfitHistoryGroupCreateDTO dto
  ) {
    log.info("/profit/link/create, Request: {}", dto);

    return ResponseEntity.ok(
      profitHistoryGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/amount/date/create")
  public ResponseEntity<ProfitAmountDateResponse> createProfitHistory(
    @RequestBody ProfitAmountDateCreateDTO dto
  ) {
    log.info("/profit/amount/date/create, Request: {}", dto);

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

  @GetMapping("/group/get/by")
  public ResponseEntity<Page<ProfitGroupResponseDTO>> getInfoProfitGroupByDate(
    @RequestParam(value = "fromDate") String fromDate,
    @RequestParam(value = "toDate") String toDate,
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfoByDate(
        lastId, fromDate, toDate, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/get/not/downloaded/by")
  public ResponseEntity<Page<ProfitGroupResponseDTO>> getInfoProfitGroupNotDownloadedByDate(
    @RequestParam(value = "fromDate") String fromDate,
    @RequestParam(value = "toDate") String toDate,
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfoByDateNotDownloaded(
        lastId, fromDate, toDate, storeId, page, size, getUser()
      )
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

  @GetMapping("/group/lastId")
  public ResponseEntity<Long> getLastIdProfitGroup(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitGroupService.getLastId(storeId)
    );
  }

  @GetMapping("/group/lastId/by")
  public ResponseEntity<Long> getLastIdProfitGroupByDate(
    @RequestParam(value = "fromDate") String fromDate,
    @RequestParam(value = "toDate") String toDate,
    @RequestParam(value = "storeId") Long storeId
    ) {
    return ResponseEntity.ok(
      profitGroupService.getLastIdByDate(storeId, fromDate, toDate)
    );
  }

  @GetMapping("/group/get/by/{global_id}")
  public ResponseEntity<ProfitGroupResponseDTO> getInfoSellGroupById(
    @PathVariable Long global_id,
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitGroupService.getInfoByDateById(
        global_id, storeId
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

  @GetMapping("/history/get/detail/by")
  public ResponseEntity<List<ProfitHistoryDetailDTO>> getProfitHistoriesByProfitGroupGlobalId(
    @RequestParam(value = "groupId") Long groupId,
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getDetailByGroupId(groupId, storeId)
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


  @GetMapping("/link/info/by")
  public ResponseEntity<List<ProfitHistoryLinkInfoDTO>> getInfoProfitHistoryGroup(
    @RequestParam(value = "groupId") Long groupId,
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      profitHistoryGroupService.getProfitHistoryLinkInfo(
        groupId, storeId, getUser()
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

  private UserEntity getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (UserEntity) authentication.getPrincipal();
  }
}