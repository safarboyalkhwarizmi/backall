package uz.backall.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.user.UserEntity;
import uz.backall.user.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final UserRepository userRepository;

  public Long create(Long userId, String storeName) {
    Optional<UserEntity> byId = userRepository.findById(userId);
    if (byId.isEmpty()) {
      throw new StoreAlreadyExistsException("Store already exists");
    }

    StoreEntity store = new StoreEntity();
    store.setName(storeName);
    store.setUserId(userId);
    storeRepository.save(store);
    return store.getId();
  }

  public List<StoreResponseDTO> getStoresByUserId(Long userId) {
    List<StoreEntity> byUserId = storeRepository.getByUserId(userId);

    List<StoreResponseDTO> result = new LinkedList<>();
    for (StoreEntity store : byUserId) {
      StoreResponseDTO response = new StoreResponseDTO();
      response.setId(store.getId());
      response.setName(store.getName());
      result.add(response);
    }

    return result;
  }

//  public List<StoreResponseDTO> getStoresByUserEmail(String email) {
//    List<StoreEntity> byUserId = storeRepository.getByUser_Email(email);
//
//    List<StoreResponseDTO> result = new LinkedList<>();
//    for (StoreEntity store : byUserId) {
//      StoreResponseDTO response = new StoreResponseDTO();
//      response.setId(store.getId());
//      response.setName(store.getName());
//      result.add(response);
//    }
//
//    return result;
//  }
}