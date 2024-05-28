package uz.backall.sell.sellGroup;

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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellGroupService {
  private final SellGroupRepository sellGroupRepository;
  private final StoreRepository storeRepository;

  public SellGroupResponseDTO create(SellGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    SellGroupEntity sellGroup = new SellGroupEntity();
    sellGroup.setAmount(dto.getAmount());
    sellGroup.setCreatedDate(dto.getCreatedDate());
    sellGroup.setStoreId(dto.getStoreId());
    sellGroupRepository.save(sellGroup);

    System.out.println(sellGroup);

    return mapToDTO(sellGroup);
  }

  public Page<SellGroupResponseDTO> getInfo(Long storeId, int page, int size, User user) {
    // Retrieve the store by ID
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<SellGroupEntity> sellGroupEntities = sellGroupRepository.findByStoreId(storeId, pageable);

    List<SellGroupResponseDTO> dtoList;
    if (user.getRole().equals(Role.BOSS)) {
      dtoList = sellGroupEntities.getContent().stream()
        .map(sellGroupEntity -> {
          sellGroupEntity.setIsOwnerDownloaded(true);
          sellGroupRepository.save(sellGroupEntity);
          return mapToDto(sellGroupEntity);
        })
        .collect(Collectors.toList());
    } else {
      dtoList = sellGroupEntities.getContent().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, sellGroupEntities.getTotalElements());
  }

  private SellGroupResponseDTO mapToDto(SellGroupEntity sellGroupEntity) {
    SellGroupResponseDTO responseDTO = new SellGroupResponseDTO();
    responseDTO.setId(sellGroupEntity.getId());
    responseDTO.setAmount(sellGroupEntity.getAmount());
    responseDTO.setCreatedDate(sellGroupEntity.getCreatedDate());
    responseDTO.setStoreId(sellGroupEntity.getStoreId());
    return responseDTO;
  }

  public Page<SellGroupResponseDTO> getInfoNotDownloaded(
    Long storeId, int page, int size, User user
  ) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellGroupEntity> byStoreProductStoreId = sellGroupRepository.findByStoreId(storeId, pageable);

    List<SellGroupResponseDTO> dtoList = byStoreProductStoreId.getContent().stream()
      .map(sellGroupEntity -> {
        sellGroupEntity.setIsOwnerDownloaded(true);
        sellGroupRepository.save(sellGroupEntity);

        return mapToDTO(sellGroupEntity);
      })
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private SellGroupResponseDTO mapToDTO(SellGroupEntity sellHistoryEntity) {
    SellGroupResponseDTO responseDTO = new SellGroupResponseDTO();
    responseDTO.setId(sellHistoryEntity.getId());
    responseDTO.setAmount(sellHistoryEntity.getAmount());
    responseDTO.setCreatedDate(sellHistoryEntity.getCreatedDate());
    responseDTO.setStoreId(sellHistoryEntity.getStoreId());
    return responseDTO;
  }
}