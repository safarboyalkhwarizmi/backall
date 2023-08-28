package uz.backall.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backall.user.User;
import uz.backall.user.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public Boolean create(Long userId, String storeName) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            return false;
        }

        StoreEntity store = new StoreEntity();
        store.setName(storeName);
        store.setUserId(userId);
        storeRepository.save(store);
        return true;
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
}