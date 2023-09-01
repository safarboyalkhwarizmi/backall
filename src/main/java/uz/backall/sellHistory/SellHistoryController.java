package uz.backall.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store/sell")
@RequiredArgsConstructor
public class SellHistoryController {
    private final SellHistoryService service;

    @PostMapping("/create")
    public ResponseEntity<Boolean> create(
            @RequestBody List<SellHistoryCreateDTO> dtoList
    ) {
        return ResponseEntity.ok(service.create(dtoList));
    }

    @GetMapping("/get/{storeId}/{date}")
    public ResponseEntity<List<SellHistoryInfoDTO>> getInfo(
            @PathVariable Long storeId, @PathVariable LocalDate date
    ) {
        return ResponseEntity.ok(service.getInfo(storeId, date));
    }
}