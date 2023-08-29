package uz.backall.sellHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}