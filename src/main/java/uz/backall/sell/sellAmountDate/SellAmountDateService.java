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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellAmountDateService {
  private final SellAmountDateRepository sellAmountDateRepository;
  private final StoreRepository storeRepository;

  public SellAmountDateResponse create(SellAmountDateCreateDTO dto) {
    SellAmountDateEntity sellAmountDate = new SellAmountDateEntity();
    sellAmountDate.setAmount(dto.getAmount());
    sellAmountDate.setDate(dto.getDate());
    sellAmountDate.setStoreId(dto.getStoreId());
    sellAmountDateRepository.save(sellAmountDate);

    return new SellAmountDateResponse(sellAmountDate.getId(), sellAmountDate.getDate(), sellAmountDate.getAmount());
  }

  public Page<SellAmountDateResponse> getInfo(
    Long storeId,
    int page,
    int size
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellAmountDateEntity> byStoreProductStoreId = sellAmountDateRepository.findByStoreId(storeId, pageable);

    List<SellAmountDateResponse> dtoList = byStoreProductStoreId.getContent().stream()
      .map(this::mapToDTO)
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private SellAmountDateResponse mapToDTO(SellAmountDateEntity sellAmountDateEntity) {
    return new SellAmountDateResponse(
      sellAmountDateEntity.getId(),
      sellAmountDateEntity.getDate(),
      sellAmountDateEntity.getAmount()
    );
  }
}
