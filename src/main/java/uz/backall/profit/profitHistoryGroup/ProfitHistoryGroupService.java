package uz.backall.profit.profitHistoryGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.backall.products.ProductEntity;
import uz.backall.products.ProductRepository;
import uz.backall.profit.profitGroup.ProfitGroupEntity;
import uz.backall.profit.profitGroup.ProfitGroupNotFoundException;
import uz.backall.profit.profitGroup.ProfitGroupRepository;
import uz.backall.profit.profitHistory.ProfitHistoryEntity;
import uz.backall.profit.profitHistory.ProfitHistoryNotFoundException;
import uz.backall.profit.profitHistory.ProfitHistoryRepository;
import uz.backall.profit.profitHistory.ProfitHistoryResponseDTO;
import uz.backall.sell.sellHistory.SellHistoryEntity;
import uz.backall.sell.sellHistory.SellHistoryResponseDTO;
import uz.backall.sell.sellHistoryGroup.SellHistoryDetailDTO;
import uz.backall.sell.sellHistoryGroup.SellHistoryGroupEntity;
import uz.backall.sell.sellHistoryGroup.SellHistoryLinkInfoDTO;
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
public class ProfitHistoryGroupService {
  private final ProfitHistoryGroupRepository profitHistoryGroupRepository;
  private final ProfitGroupRepository profitGroupRepository;
  private final ProfitHistoryRepository profitHistoryRepository;
  private final StoreRepository storeRepository;
  private final ProductRepository productRepository;

  public ProfitHistoryGroupResponseDTO create(ProfitHistoryGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Optional<ProfitGroupEntity> byGroupId =
      profitGroupRepository.findById(dto.getProfitGroupId());
    if (byGroupId.isEmpty()) {
      throw new ProfitGroupNotFoundException("Sell group not found");
    }

    Optional<ProfitHistoryEntity> byHistoryId =
      profitHistoryRepository.findById(dto.getProfitHistoryId());
    if (byHistoryId.isEmpty()) {
      throw new ProfitHistoryNotFoundException("Sell history not found");
    }

    ProfitHistoryGroupEntity profitHistoryGroup = new ProfitHistoryGroupEntity();
    profitHistoryGroup.setProfitGroupId(dto.getProfitGroupId());
    profitHistoryGroup.setProfitHistoryId(dto.getProfitHistoryId());
    profitHistoryGroup.setStoreId(dto.getStoreId());
    profitHistoryGroupRepository.save(profitHistoryGroup);

    return new ProfitHistoryGroupResponseDTO(
      profitHistoryGroup.getId(),
      profitHistoryGroup.getProfitGroupId(),
      profitHistoryGroup.getProfitHistoryId()
    );
  }

  public Page<ProfitHistoryGroupResponseDTO> getInfo(
    Long lastId, Long storeId, int page, int size, User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitHistoryGroupEntity> byStoreId =
      profitHistoryGroupRepository.findByIdLessThanAndIdGreaterThanAndStoreId(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitHistoryGroupResponseDTO> dtoList;
    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreId.getContent().stream()
        .map(profitHistoryGroupEntity -> {
          profitHistoryGroupEntity.setIsOwnerDownloaded(true);
          profitHistoryGroupRepository.save(profitHistoryGroupEntity);

          return mapToDTO(profitHistoryGroupEntity);
        })
        .collect(Collectors.toList());
    } else {
      dtoList = byStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }

  public Page<ProfitHistoryGroupResponseDTO> getInfoNotDownloaded(
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
    Page<ProfitHistoryGroupEntity> byStoreId =
      profitHistoryGroupRepository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitHistoryGroupResponseDTO> dtoList =
      byStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreId.getTotalElements());
  }


  private ProfitHistoryGroupResponseDTO mapToDTO(ProfitHistoryGroupEntity profitHistoryGroupEntity) {
    return new ProfitHistoryGroupResponseDTO(profitHistoryGroupEntity.getId(), profitHistoryGroupEntity.getProfitGroupId(), profitHistoryGroupEntity.getProfitHistoryId());
  }

  public Long getLastId(Long storeId) {
    return profitHistoryGroupRepository.findTop1ByStoreIdOrderByIdDesc(storeId)
      .map(ProfitHistoryGroupEntity::getId)
      .orElseThrow(() -> new ProfitHistoryGroupNotFoundException("No ProfitHistoryGroup found for storeId: " + storeId));
  }

  public List<ProfitHistoryDetailDTO> getDetailByGroupId(Long groupId, Long storeId) {
    List<ProfitHistoryDetailDTO> profitHistoryDetails = new ArrayList<>();

    List<ProfitHistoryGroupEntity> byStoreIdAndProfitGroupId = profitHistoryGroupRepository.findByStoreIdAndProfitGroupId(storeId, groupId);
    for (ProfitHistoryGroupEntity profitHistoryGroupEntity : byStoreIdAndProfitGroupId) {
      Optional<ProfitHistoryEntity> profitHistoryById = profitHistoryRepository.findById(profitHistoryGroupEntity.getId());
      if (profitHistoryById.isEmpty()) {
        continue;
      }

      ProfitHistoryEntity profitHistory = profitHistoryById.get();

      Optional<ProductEntity> productByProductId = productRepository.findById(profitHistory.getProductId());
      if (productByProductId.isEmpty()) {
        continue;
      }

      ProfitHistoryDetailDTO dto = new ProfitHistoryDetailDTO();
      dto.setId(profitHistory.getId());
      dto.setName(productByProductId.get().getName());
      dto.setCount_type(productByProductId.get().getType().name());
      dto.setSaved(profitHistory.getIsOwnerDownloaded());
      dto.setProfit(profitHistory.getProfit());
      dto.setCount(profitHistory.getCount());
      dto.setCreated_date(profitHistory.getCreatedDate());
      dto.setProduct_id(profitHistory.getProductId());
      profitHistoryDetails.add(dto);
    }

    return profitHistoryDetails;
  }


  public List<ProfitHistoryLinkInfoDTO> getProfitHistoryLinkInfo(
    Long groupId,
    Long storeId,
    User user
  ) {
    List<ProfitHistoryGroupEntity> byStoreIdAndProfitGroupId = profitHistoryGroupRepository.findByStoreIdAndProfitGroupIdGreaterThanEqual(storeId, groupId);
    List<ProfitHistoryLinkInfoDTO> profitLinkDTOList = new ArrayList<>();
    for (ProfitHistoryGroupEntity profitHistoryGroupEntity : byStoreIdAndProfitGroupId) {
      ProfitHistoryLinkInfoDTO profitLinkDTO = new ProfitHistoryLinkInfoDTO();
      profitLinkDTO.setId(profitHistoryGroupEntity.getId());
      profitLinkDTO.setProfitHistoryId(profitHistoryGroupEntity.getProfitHistoryId());
      profitLinkDTO.setProfitGroupId(profitHistoryGroupEntity.getProfitGroupId());

      ProfitHistoryResponseDTO profitHistoryResponseDTO = getProfitHistoryResponseDTO(profitHistoryGroupEntity);
      profitLinkDTO.setProfitHistory(profitHistoryResponseDTO);

      profitLinkDTOList.add(profitLinkDTO);
    }

    return profitLinkDTOList;
  }

  private ProfitHistoryResponseDTO getProfitHistoryResponseDTO(ProfitHistoryGroupEntity profitHistoryGroupEntity) {
    ProfitHistoryResponseDTO profitHistoryResponseDTO = new ProfitHistoryResponseDTO();
    profitHistoryResponseDTO.setId(profitHistoryGroupEntity.getProfitHistory().getId());
    profitHistoryResponseDTO.setCount(profitHistoryGroupEntity.getProfitHistory().getCount());
    profitHistoryResponseDTO.setProfit(profitHistoryGroupEntity.getProfitHistory().getProfit());
    profitHistoryResponseDTO.setCountType(profitHistoryGroupEntity.getProfitHistory().getCountType());
    profitHistoryResponseDTO.setCreatedDate(profitHistoryGroupEntity.getProfitHistory().getCreatedDate());
    profitHistoryResponseDTO.setProductId(profitHistoryGroupEntity.getProfitHistory().getProductId());
    profitHistoryResponseDTO.setStoreId(profitHistoryGroupEntity.getProfitHistory().getStoreId());
    return profitHistoryResponseDTO;
  }
}