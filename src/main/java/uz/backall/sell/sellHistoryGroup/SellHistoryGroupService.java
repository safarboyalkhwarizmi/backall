package uz.backall.sell.sellHistoryGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductRepository;
import uz.backall.sell.sellGroup.SellGroupEntity;
import uz.backall.sell.sellGroup.SellGroupNotFoundException;
import uz.backall.sell.sellGroup.SellGroupRepository;
import uz.backall.sell.sellHistory.SellHistoryEntity;
import uz.backall.sell.sellHistory.SellHistoryNotFoundException;
import uz.backall.sell.sellHistory.SellHistoryRepository;
import uz.backall.sell.sellHistory.SellHistoryResponseDTO;
import uz.backall.store.StoreEntity;
import uz.backall.store.StoreNotFoundException;
import uz.backall.store.StoreRepository;
import uz.backall.user.Role;
import uz.backall.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellHistoryGroupService {
  private final SellHistoryGroupRepository sellHistoryGroupRepository;
  private final SellGroupRepository sellGroupRepository;
  private final SellHistoryRepository sellHistoryRepository;
  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;

  public SellHistoryGroupResponseDTO create(SellHistoryGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Optional<SellGroupEntity> byGroupId = sellGroupRepository.findById(dto.getSellGroupId());
    if (byGroupId.isEmpty()) {
      throw new SellGroupNotFoundException("Sell group not found");
    }

    Optional<SellHistoryEntity> byHistoryId = sellHistoryRepository.findById(dto.getSellHistoryId());
    if (byHistoryId.isEmpty()) {
      throw new SellHistoryNotFoundException("Sell history not found");
    }

    SellHistoryGroupEntity sellHistoryGroup = new SellHistoryGroupEntity();
    sellHistoryGroup.setSellGroupId(dto.getSellGroupId());
    sellHistoryGroup.setSellHistoryId(dto.getSellHistoryId());
    sellHistoryGroup.setStoreId(dto.getStoreId());
    sellHistoryGroupRepository.save(sellHistoryGroup);

    return new SellHistoryGroupResponseDTO(
      sellHistoryGroup.getId(),
      sellHistoryGroup.getSellGroupId(),
      sellHistoryGroup.getSellHistoryId()
    );
  }

  public Page<SellHistoryGroupResponseDTO> getInfo(
    Long lastId,
    Long storeId,
    int page,
    int size,
    User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<SellHistoryGroupEntity> byStoreId =
      sellHistoryGroupRepository.findByIdLessThanAndIdGreaterThanAndStoreId(
        lastId, lastId - size, storeId, pageable
      );

    List<SellHistoryGroupResponseDTO> dtoList;
    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreId.getContent().stream()
        .map(sellHistoryGroupEntity -> {
          sellHistoryGroupEntity.setIsOwnerDownloaded(true);
          sellHistoryGroupRepository.save(sellHistoryGroupEntity);

          return mapToDTO(sellHistoryGroupEntity);
        })
        .collect(Collectors.toList());
    } else {
      dtoList = byStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }

  public Page<SellHistoryGroupResponseDTO> getInfoNotDownloaded(
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
    Page<SellHistoryGroupEntity> byStoreId =
      sellHistoryGroupRepository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(
        lastId, lastId - size, storeId, pageable
      );

    List<SellHistoryGroupResponseDTO> dtoList =
      byStoreId.getContent().stream()
        .map(sellHistoryGroupEntity -> {
          sellHistoryGroupEntity.setIsOwnerDownloaded(true);
          sellHistoryGroupRepository.save(sellHistoryGroupEntity);

          return mapToDTO(sellHistoryGroupEntity);
        })
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }

  private SellHistoryGroupResponseDTO mapToDTO(SellHistoryGroupEntity sellHistoryGroupEntity) {
    return new SellHistoryGroupResponseDTO(
      sellHistoryGroupEntity.getId(),
      sellHistoryGroupEntity.getSellGroupId(),
      sellHistoryGroupEntity.getSellHistoryId()
    );
  }

  public Long getLastId(Long storeId) {
    return sellHistoryGroupRepository.findTop1ByStoreIdOrderByIdDesc(storeId)
      .map(SellHistoryGroupEntity::getId)
      .orElseThrow(() -> new SellHistoryGroupNotFoundException("No SellHistoryGroup found for storeId: " + storeId));
  }

  public List<SellHistoryDetailDTO> getDetailByGroupId(Long storeId, Long groupId) {
    List<SellHistoryDetailDTO> sellHistoryDetails = new ArrayList<>();

    List<SellHistoryGroupEntity> byStoreIdAndSellGroupId = sellHistoryGroupRepository.findByStoreIdAndSellGroupId(storeId, groupId);
    for (SellHistoryGroupEntity sellHistoryGroupEntity : byStoreIdAndSellGroupId) {
      Optional<SellHistoryEntity> sellHistoryById = sellHistoryRepository.findById(sellHistoryGroupEntity.getId());
      if (sellHistoryById.isEmpty()) {
        continue;
      }

      SellHistoryEntity sellHistory = sellHistoryById.get();

      Optional<ProductEntity> productByProductId = productRepository.findById(sellHistory.getProductId());
      if (productByProductId.isEmpty()) {
        continue;
      }

      SellHistoryDetailDTO dto = new SellHistoryDetailDTO();
      dto.setId(sellHistory.getId());
      dto.setName(productByProductId.get().getName());
      dto.setCount_type(productByProductId.get().getType().name());
      dto.setSaved(sellHistory.getIsOwnerDownloaded());
      dto.setSelling_price(sellHistory.getSellingPrice());
      dto.setCount(sellHistory.getCount());
      dto.setCreated_date(sellHistory.getCreatedDate());
      dto.setProduct_id(sellHistory.getProductId());
      sellHistoryDetails.add(dto);
    }

    return sellHistoryDetails;
  }

  public List<SellHistoryLinkInfoDTO> getSellHistoryLinkInfo(
    Long groupId,
    Long storeId,
    User user
  ) {
    List<SellHistoryGroupEntity> byStoreIdAndSellGroupId = sellHistoryGroupRepository.findByStoreIdAndSellGroupId(storeId, groupId);
    List<SellHistoryLinkInfoDTO> sellLinkDTOList = new ArrayList<>();
    for (SellHistoryGroupEntity sellHistoryGroupEntity : byStoreIdAndSellGroupId) {
      SellHistoryLinkInfoDTO sellLinkDTO = new SellHistoryLinkInfoDTO();
      sellLinkDTO.setId(sellHistoryGroupEntity.getId());
      sellLinkDTO.setSellHistoryId(sellHistoryGroupEntity.getSellHistoryId());
      sellLinkDTO.setSellGroupId(groupId);

      SellHistoryResponseDTO sellHistoryResponseDTO = getSellHistoryResponseDTO(sellHistoryGroupEntity);
      sellLinkDTO.setSellHistory(sellHistoryResponseDTO);

      sellLinkDTOList.add(sellLinkDTO);
    }

    return sellLinkDTOList;
  }

  private SellHistoryResponseDTO getSellHistoryResponseDTO(SellHistoryGroupEntity sellHistoryGroupEntity) {
    SellHistoryResponseDTO sellHistoryResponseDTO = new SellHistoryResponseDTO();
    sellHistoryResponseDTO.setId(sellHistoryGroupEntity.getSellHistory().getId());
    sellHistoryResponseDTO.setCount(sellHistoryGroupEntity.getSellHistory().getCount());
    sellHistoryResponseDTO.setSellingPrice(sellHistoryGroupEntity.getSellHistory().getSellingPrice());
    sellHistoryResponseDTO.setCountType(sellHistoryGroupEntity.getSellHistory().getCountType());
    sellHistoryResponseDTO.setCreatedDate(sellHistoryGroupEntity.getSellHistory().getCreatedDate());
    sellHistoryResponseDTO.setProductId(sellHistoryGroupEntity.getSellHistory().getProductId());
    sellHistoryResponseDTO.setStoreId(sellHistoryGroupEntity.getSellHistory().getStoreId());
    return sellHistoryResponseDTO;
  }
}