package uz.backall.sell.sellAmountDate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.sell.sellGroup.SellGroupEntity;
import uz.backall.sell.sellGroup.SellGroupResponseDTO;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;
import uz.backall.user.Role;
import uz.backall.user.User;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellAmountDateService {
  private final SellAmountDateRepository sellAmountDateRepository;
  private final StoreRepository storeRepository;

  public SellAmountDateResponse create(SellAmountDateCreateDTO dto) {
    Optional<SellAmountDateEntity> byStoreIdAndDate = sellAmountDateRepository.findByStoreIdAndDate(dto.getStoreId(), dto.getDate());

    SellAmountDateEntity sellAmountDate;
    if (byStoreIdAndDate.isEmpty()) {
      sellAmountDate = new SellAmountDateEntity();
      sellAmountDate.setAmount(dto.getAmount());
      sellAmountDate.setDate(dto.getDate());
      sellAmountDate.setStoreId(dto.getStoreId());
      sellAmountDateRepository.save(sellAmountDate);
    } else {
      sellAmountDate = byStoreIdAndDate.get();
      sellAmountDate.setAmount(dto.getAmount());
      sellAmountDateRepository.save(sellAmountDate);
    }

    return new SellAmountDateResponse(sellAmountDate.getId(), sellAmountDate.getDate(), sellAmountDate.getAmount());
  }

  public Page<SellAmountDateResponse> getInfo(
    Long lastId, Long storeId, int page, int size, User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellAmountDateEntity> byStoreProductStoreId = sellAmountDateRepository.findByIdLessThanAndIdGreaterThanAndStoreId(
      lastId, lastId - size, storeId, pageable
    );
    List<SellAmountDateResponse> dtoList;

    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(sellAmountDateEntity -> {
          sellAmountDateEntity.setIsOwnerDownloaded(true);
          sellAmountDateRepository.save(sellAmountDateEntity);

          return new SellAmountDateResponse(
            sellAmountDateEntity.getId(),
            sellAmountDateEntity.getDate(),
            sellAmountDateEntity.getAmount()
          );
        })
        .collect(Collectors.toList());
    } else {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(sellAmountDateEntity -> {
          return new SellAmountDateResponse(
            sellAmountDateEntity.getId(),
            sellAmountDateEntity.getDate(),
            sellAmountDateEntity.getAmount()
          );
        })
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public SellAmountDateResponse getInfoByDate(String date, Long storeId) {
    // Check if the store exists
    StoreEntity store = storeRepository.findById(storeId)
      .orElseThrow(() -> new StoreNotFoundException("Store not found for id: " + storeId));

    // Find the SellAmountDateEntity by storeId and date
    SellAmountDateEntity sellAmountDate = sellAmountDateRepository.findByStoreIdAndDate(storeId, date)
      .orElseThrow(() -> new SellAmountDateNotFoundException("SellAmountDate not found for storeId: " + storeId));

    // Return the response with the necessary details
    return new SellAmountDateResponse(
      sellAmountDate.getId(),
      sellAmountDate.getDate(),
      sellAmountDate.getAmount()
    );
  }

  public Page<SellAmountDateResponse> getInfoNotDownloaded(
    Long lastId,
    Long storeId,
    int page,
    int size,
    User user
  ) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellAmountDateEntity> byStoreProductStoreId =
      sellAmountDateRepository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(
        lastId, lastId - size, storeId, pageable
      );

    List<SellAmountDateResponse> dtoList =
      byStoreProductStoreId.getContent().stream()
        .map(sellAmountDateEntity -> {
          sellAmountDateEntity.setIsOwnerDownloaded(true);
          sellAmountDateRepository.save(sellAmountDateEntity);

          return new SellAmountDateResponse(
            sellAmountDateEntity.getId(),
            sellAmountDateEntity.getDate(),
            sellAmountDateEntity.getAmount()
          );
        })
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public Long getLastId(Long storeId) {
    return sellAmountDateRepository.findTop1ByStoreIdOrderByIdDesc(storeId)
      .map(SellAmountDateEntity::getId)
      .orElseThrow(() -> new SellAmountDateNotFoundException("No SellAmountDate found for storeId: " + storeId));
  }

  public Long getMonthAmount(Long storeId) {
    Calendar calendar = Calendar.getInstance();
    int monthNumber = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
    int year = calendar.get(Calendar.YEAR);

    // Construct the date pattern for the given month and year (e.g., "2024-08-%")
    String datePattern = year + "-" + (monthNumber < 10 ? "0" : "") + monthNumber + "-%";

    Long totalAmount = sellAmountDateRepository.findTotalAmountByStoreIdAndDatePattern(storeId, datePattern);
    return totalAmount != null ? totalAmount : 0;
  }
}
