package uz.backall.profit.profitAmountDate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
public class ProfitAmountDateService {
  private final ProfitAmountDateRepository profitAmountDateRepository;
  private final StoreRepository storeRepository;

  public ProfitAmountDateResponse create(ProfitAmountDateCreateDTO dto) {
    Optional<ProfitAmountDateEntity> byStoreIdAndDate = profitAmountDateRepository.findByStoreIdAndDate(dto.getStoreId(), dto.getDate());

    ProfitAmountDateEntity profitAmountDate;
    if (byStoreIdAndDate.isEmpty()) {
      profitAmountDate = new ProfitAmountDateEntity();
      profitAmountDate.setAmount(dto.getAmount());
      profitAmountDate.setDate(dto.getDate());
      profitAmountDate.setStoreId(dto.getStoreId());
      profitAmountDateRepository.save(profitAmountDate);
    } else {
      profitAmountDate = byStoreIdAndDate.get();
      profitAmountDate.setAmount(dto.getAmount());
      profitAmountDateRepository.save(profitAmountDate);
    }

    return new ProfitAmountDateResponse(
      profitAmountDate.getId(),
      profitAmountDate.getDate(),
      profitAmountDate.getAmount()
    );
  }

  public Page<ProfitAmountDateResponse> getInfo(
    Long lastId, Long storeId, int page, int size, User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitAmountDateEntity> byStoreProductStoreId =
      profitAmountDateRepository.findByIdLessThanAndIdGreaterThanAndStoreId(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitAmountDateResponse> dtoList;
    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(profitAmountDateEntity -> {
          profitAmountDateEntity.setIsOwnerDownloaded(true);
          profitAmountDateRepository.save(profitAmountDateEntity);

          return mapToDTO(profitAmountDateEntity);
        })
        .collect(Collectors.toList());
    } else {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public Page<ProfitAmountDateResponse> getInfoNotDownloaded(
    Long lastId, Long storeId, int page, int size, User user
  ) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitAmountDateEntity> byStoreProductStoreId =
      profitAmountDateRepository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitAmountDateResponse> dtoList = byStoreProductStoreId.getContent().stream()
      .map(profitAmountDateEntity -> {
        profitAmountDateEntity.setIsOwnerDownloaded(true);
        profitAmountDateRepository.save(profitAmountDateEntity);

        return mapToDTO(profitAmountDateEntity);
      })
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private ProfitAmountDateResponse mapToDTO(ProfitAmountDateEntity profitAmountDateEntity) {
    return new ProfitAmountDateResponse(
      profitAmountDateEntity.getId(),
      profitAmountDateEntity.getDate(),
      profitAmountDateEntity.getAmount()
    );
  }

  public Long getLastId(Long storeId) {
    return profitAmountDateRepository.findTop1ByStoreIdOrderByIdDesc(storeId).getId();
  }

  public ProfitAmountDateResponse getInfoByDate(String date, Long storeId) {
    Optional<ProfitAmountDateEntity> byStoreIdAndDate =
      profitAmountDateRepository.findByStoreIdAndDate(storeId, date);
    if (byStoreIdAndDate.isEmpty()) {
      throw new ProfitAmountNotFoundException("Profit amount not found");
    }

    ProfitAmountDateEntity profitAmountDate = byStoreIdAndDate.get();

    return new ProfitAmountDateResponse(
      profitAmountDate.getId(),
      profitAmountDate.getDate(),
      profitAmountDate.getAmount()
    );
  }

  public Double getMonthAmount(Long storeId) {
    Calendar calendar = Calendar.getInstance();
    int monthNumber = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
    int year = calendar.get(Calendar.YEAR);

    // Construct the date pattern for the given month and year (e.g., "2024-08-%")
    String datePattern = year + "-" + (monthNumber < 10 ? "0" : "") + monthNumber + "-%";

    Double totalAmount = profitAmountDateRepository.findTotalAmountByStoreIdAndDatePattern(storeId, datePattern);
    return totalAmount != null ? totalAmount : 0.0;
  }
}