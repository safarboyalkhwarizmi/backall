package uz.backall.sell;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.backall.sell.sellAmountDate.SellAmountDateCreateDTO;
import uz.backall.sell.sellAmountDate.SellAmountDateResponse;
import uz.backall.sell.sellAmountDate.SellAmountDateService;
import uz.backall.sell.sellGroup.SellGroupCreateDTO;
import uz.backall.sell.sellGroup.SellGroupResponseDTO;
import uz.backall.sell.sellGroup.SellGroupService;
import uz.backall.sell.sellHistory.SellHistoryCreateDTO;
import uz.backall.sell.sellHistory.SellHistoryInfoDTO;
import uz.backall.sell.sellHistory.SellHistoryResponseDTO;
import uz.backall.sell.sellHistory.SellHistoryService;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupCreateDTO;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupResponseDTO;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupService;
import uz.backall.user.User;

@RestController
@RequestMapping("/api/v1/store/sell")
@RequiredArgsConstructor
public class SellController {
  private final SellGroupService sellGroupService;
  private final SellHistoryService sellHistoryService;
  private final SellHistoryGroupService sellHistoryGroupService;
  private final SellAmountDateService sellAmountDateService;

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/group/create")
  public ResponseEntity<SellGroupResponseDTO> createSellGroup(
    @RequestBody SellGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      sellGroupService.create(dto)
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

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/link/create")
  public ResponseEntity<SellHistoryGroupResponseDTO> createSellHistory(
    @RequestBody SellHistoryGroupCreateDTO dto
  ) {
    return ResponseEntity.ok(
      sellHistoryGroupService.create(dto)
    );
  }

  @PreAuthorize("hasAnyRole('SELLER', 'SELLER_BOSS')")
  @PostMapping("/amount/date/create")
  public ResponseEntity<SellAmountDateResponse> createSellAmountDate(
    @RequestBody SellAmountDateCreateDTO dto
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.create(dto)
    );
  }

  @GetMapping("/amount/date/get")
  public ResponseEntity<Page<SellAmountDateResponse>> getSellAmountDateInfo(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/month/amount")
  public ResponseEntity<Long> getThisMonthAmountInfo(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.getMonthAmount(
        storeId
      )
    );
  }

  @GetMapping("/amount/date/get/by")
  public ResponseEntity<SellAmountDateResponse> getSellAmountDateInfoByDate(
    @RequestParam(value = "date") String date,
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.getInfoByDate(
        date, storeId
      )
    );
  }

  @GetMapping("/amount/date/get/not/downloaded")
  public ResponseEntity<Page<SellAmountDateResponse>> getSellAmountDateInfoNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/amount/date/get/lastId")
  public ResponseEntity<Long> getLastIdAmountDate(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellAmountDateService.getLastId(storeId)
    );
  }

  @GetMapping("/group/get")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfoSellGroup(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellGroupService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/get")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfoSellGroupByDate(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "fromDate") String fromDate,
    @RequestParam(value = "toDate") String toDate,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellGroupService.getInfoByDate(
        lastId, fromDate, toDate, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/get/lastId")
  public ResponseEntity<Long> getLastIdSellGroup(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellGroupService.getLastId(storeId)
    );
  }

  @GetMapping("/group/get/not/downloaded")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfoSellGroupNotDownloadedByDate(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "fromDate") String fromDate,
    @RequestParam(value = "toDate") String toDate,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellGroupService.getInfoNotDownloadedByDate(
        lastId, fromDate, toDate, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/group/get/not/downloaded")
  public ResponseEntity<Page<SellGroupResponseDTO>> getInfoSellGroupNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellGroupService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/history/get")
  public ResponseEntity<Page<SellHistoryInfoDTO>> getInfoSellHistory(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellHistoryService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/history/get/lastId")
  public ResponseEntity<Long> getLastIdSellHistory(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellHistoryService.getLastId(storeId)
    );
  }

  @GetMapping("/history/get/not/downloaded")
  public ResponseEntity<Page<SellHistoryInfoDTO>> getInfoSellHistoryNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellHistoryService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/link/info")
  public ResponseEntity<Page<SellHistoryGroupResponseDTO>> getInfoSellHistoryGroup(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellHistoryGroupService.getInfo(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  @GetMapping("/link/get/lastId")
  public ResponseEntity<Long> getLastIdSellHistoryGroup(
    @RequestParam(value = "storeId") Long storeId
  ) {
    return ResponseEntity.ok(
      sellHistoryGroupService.getLastId(storeId)
    );
  }

  @GetMapping("/link/info/not/downloaded")
  public ResponseEntity<Page<SellHistoryGroupResponseDTO>> getInfoSellHistoryGroupNotDownloaded(
    @RequestParam(value = "lastId") Long lastId,
    @RequestParam(value = "storeId") Long storeId,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(
      sellHistoryGroupService.getInfoNotDownloaded(
        lastId, storeId, page, size, getUser()
      )
    );
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (User) authentication.getPrincipal();
  }
}