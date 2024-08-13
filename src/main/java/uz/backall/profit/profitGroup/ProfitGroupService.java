package uz.backall.profit.profitGroup;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfitGroupService {
  private final ProfitGroupRepository repository;
  private final StoreRepository storeRepository;

  public ProfitGroupResponseDTO create(ProfitGroupCreateDTO dto) {
    Optional<StoreEntity> storeById = storeRepository.findById(dto.getStoreId());
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    ProfitGroupEntity profitGroup = new ProfitGroupEntity();
    profitGroup.setProfit(dto.getProfit());
    profitGroup.setCreatedDate(dto.getCreatedDate());
    profitGroup.setStoreId(dto.getStoreId());
    repository.save(profitGroup);

    return mapToDTO(profitGroup);
  }

  public Page<ProfitGroupResponseDTO> getInfo(
    Long lastId, Long storeId, int page, int size, User user
  ) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitGroupEntity> byStoreProductStoreId =
      repository.findByIdLessThanAndIdGreaterThanAndStoreId(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitGroupResponseDTO> dtoList;

      if (user.getRole().equals(Role.BOSS)) {
        dtoList = byStoreProductStoreId.getContent().stream()
          .map(profitGroupEntity -> {
            profitGroupEntity.setIsOwnerDownloaded(true);
            repository.save(profitGroupEntity);

            return mapToDTO(profitGroupEntity);
          })
          .collect(Collectors.toList());
      } else {
        dtoList = byStoreProductStoreId.getContent().stream()
          .map(this::mapToDTO)
          .collect(Collectors.toList());
      }

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public Page<ProfitGroupResponseDTO> getInfoNotDownloaded(
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
    Page<ProfitGroupEntity> byStoreProductStoreId =
      repository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalse(
        lastId, lastId - size, storeId, pageable
      );

    List<ProfitGroupResponseDTO> dtoList =
      byStoreProductStoreId.getContent().stream()
      .map(profitGroupEntity -> {
        profitGroupEntity.setIsOwnerDownloaded(true);
        repository.save(profitGroupEntity);

        return mapToDTO(profitGroupEntity);
      })
      .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  private ProfitGroupResponseDTO mapToDTO(ProfitGroupEntity profitHistoryEntity) {
    ProfitGroupResponseDTO responseDTO = new ProfitGroupResponseDTO();
    responseDTO.setId(profitHistoryEntity.getId());
    responseDTO.setProfit(profitHistoryEntity.getProfit());
    responseDTO.setCreatedDate(profitHistoryEntity.getCreatedDate());
    responseDTO.setStoreId(profitHistoryEntity.getStoreId());
    return responseDTO;
  }

  public Long getLastId(Long storeId) {
    return repository.findTop1ByStoreIdOrderByIdDesc(storeId)
      .map(ProfitGroupEntity::getId)
      .orElseThrow(() -> new ProfitGroupNotFoundException("No ProfitGroup found for storeId: " + storeId));
  }

  public Page<ProfitGroupResponseDTO> getInfoByDate(Long lastId, String fromDate, String toDate, Long storeId, int page, int size, User user) {
    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    LocalDate fromLocalDate = LocalDate.parse(fromDate);
    LocalDateTime fromLocalDateTime = fromLocalDate.atTime(0, 0, 0, 0);

    LocalDate toLocalDate = LocalDate.parse(toDate);
    LocalDateTime toLocalDateTime = toLocalDate.atTime(23, 59, 59, 999);

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitGroupEntity> byStoreProductStoreId =
      repository.findByIdLessThanAndIdGreaterThanAndStoreIdAndCreatedDateBetween(
        lastId, lastId - size, storeId, toLocalDateTime, fromLocalDateTime, pageable
      );

    List<ProfitGroupResponseDTO> dtoList;

    if (user.getRole().equals(Role.BOSS)) {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(profitGroupEntity -> {
          profitGroupEntity.setIsOwnerDownloaded(true);
          repository.save(profitGroupEntity);

          return mapToDTO(profitGroupEntity);
        })
        .collect(Collectors.toList());
    }
    else {
      dtoList = byStoreProductStoreId.getContent().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }

  public Page<ProfitGroupResponseDTO> getInfoByDateNotDownloaded(Long lastId, String fromDate, String toDate, Long storeId, int page, int size, User user) {
    if (!user.getRole().equals(Role.BOSS)) {
      return Page.empty();
    }

    Optional<StoreEntity> storeById = storeRepository.findById(storeId);
    if (storeById.isEmpty()) {
      throw new StoreNotFoundException("Store not found");
    }

    LocalDate fromLocalDate = LocalDate.parse(fromDate);
    LocalDateTime fromLocalDateTime = fromLocalDate.atTime(0, 0, 0, 0);

    LocalDate toLocalDate = LocalDate.parse(toDate);
    LocalDateTime toLocalDateTime = toLocalDate.atTime(23, 59, 59, 999);

    Pageable pageable = PageRequest.of(page, size);
    Page<ProfitGroupEntity> byStoreProductStoreId =
      repository.findByIdLessThanAndIdGreaterThanAndStoreIdAndIsOwnerDownloadedFalseAndCreatedDateBetween(
        lastId, lastId - size, storeId, toLocalDateTime, fromLocalDateTime, pageable
      );

    List<ProfitGroupResponseDTO> dtoList =
      byStoreProductStoreId.getContent().stream()
        .map(profitGroupEntity -> {
          profitGroupEntity.setIsOwnerDownloaded(true);
          repository.save(profitGroupEntity);

          return mapToDTO(profitGroupEntity);
        })
        .collect(Collectors.toList());

    return new PageImpl<>(dtoList, pageable, byStoreProductStoreId.getTotalElements());
  }
}