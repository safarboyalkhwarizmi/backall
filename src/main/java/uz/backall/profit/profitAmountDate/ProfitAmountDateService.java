package uz.backall.profit.profitAmountDate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.store.StoreNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitAmountDateService {
  private final ProfitAmountDateRepository profitAmountDateRepository;

  public ProfitAmountDateResponse create(ProfitAmountDateCreateDTO dto) {
    ProfitAmountDateEntity profitAmountDate = new ProfitAmountDateEntity();
    profitAmountDate.setAmount(dto.getAmount());
    profitAmountDate.setDate(dto.getDate());
    profitAmountDate.setStoreId(dto.getStoreId());
    profitAmountDateRepository.save(profitAmountDate);

    return new ProfitAmountDateResponse(
      profitAmountDate.getId(),
      profitAmountDate.getDate(),
      profitAmountDate.getAmount()
    );
  }

  public Page<ProfitAmountDateResponse> getInfo(
    Long storeId,
    int page,
    int size
  ) {
    Optional<ProfitAmountDateEntity> storeById = profitAmountDateRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitAmountDateEntity> byStoreProductStoreId =
      profitAmountDateRepository.findByStoreId(storeId, pageable);

    List<ProfitAmountDateResponse> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
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
}
