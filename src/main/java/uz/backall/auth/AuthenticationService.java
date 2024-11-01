package uz.backall.auth;

import com.google.gson.Gson;
import io.swagger.v3.core.util.Json;
import uz.backall.config.jwt.JwtService;
import uz.backall.idempotencyKey.IdempotencyKeyEntity;
import uz.backall.idempotencyKey.IdempotencyKeyRepository;
import uz.backall.idempotencyKey.IdempotencyKeyUsedException;
import uz.backall.store.StoreService;
import uz.backall.token.Token;
import uz.backall.token.TokenRepository;
import uz.backall.token.TokenType;
import uz.backall.user.Role;
import uz.backall.user.UserEntity;
import uz.backall.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import uz.backall.util.MD5;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final StoreService storeService;
  private final IdempotencyKeyRepository idempotencyKeyRepository;

  public AuthenticationResponse register(String idempotencyKey, RegisterRequest request) {
    IdempotencyKeyEntity savedKey = idempotencyKeyRepository.findById(idempotencyKey).orElse(null);

    if (savedKey != null) {
      if (savedKey.getExpiryDate().isBefore(LocalDateTime.now())) {
        idempotencyKeyRepository.delete(savedKey);
      } else {
        Gson gson = new Gson();
        AuthenticationResponse authenticationResponse = gson.fromJson(savedKey.getResponse(), AuthenticationResponse.class);
        if (authenticationResponse.getAccessToken() == null) {
          throw new IdempotencyKeyUsedException("Idempotency-Key used by other process");
        }

        return authenticationResponse;
      }
    }

    Optional<UserEntity> byEmail = repository.findByEmailAndPinCode(
      request.getEmail(),
      request.getPinCode()
    );
    if (byEmail.isPresent()) {
      throw new UserAlreadyExistsException("User already exists.");
    }

    var user = UserEntity.builder()
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .phone(request.getPhone())
      .email(request.getEmail())
      .password(MD5.md5(request.getPassword()))
      .pinCode(request.getPinCode())
      .role(Role.SELLER_BOSS)
      .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    Long storeId = storeService.create(savedUser.getId(), request.getStoreName());
    saveUserToken(savedUser, jwtToken);
    var response = AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .role(Role.SELLER_BOSS)
      .storeId(storeId)
      .build();

    saveIdempotencyKey(idempotencyKey, Json.pretty(response));
    return response;
  }

  private void saveIdempotencyKey(String idempotencyKey, String responseJson) {
    IdempotencyKeyEntity newKey = new IdempotencyKeyEntity();
    newKey.setKey(idempotencyKey);
    newKey.setResponse(responseJson);
    newKey.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24-hour expiration
    idempotencyKeyRepository.save(newKey);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    List<UserEntity> byEmailAndPassword = repository.findByEmailAndPassword(
      request.getEmail(), MD5.md5(request.getPassword())
    );
    if (byEmailAndPassword.isEmpty()) {
      throw new UserNotFoundException("ACCOUNT NOT FOUND!");
    }

    var user = repository.findByEmailAndPinCode(request.getEmail(), request.getPinCode())
      .orElseThrow();

    Optional<UserEntity> byEmailAndRole = repository.findByEmailAndRole(user.getEmail(), Role.BOSS);
    if (byEmailAndRole.isEmpty()) {
      throw new BossNotFoundException("Boss not found");
    }

    UserEntity bossProfile = byEmailAndRole.get();
    Long storeId = storeService.getStoresByUserId(bossProfile.getId()).get(0).getId();

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .role(user.getRole())
      .storeId(storeId)
      .build();
  }

  private void saveUserToken(UserEntity userEntity, String jwtToken) {
    var token = Token.builder()
      .userEntity(userEntity)
      .token(jwtToken)
      .tokenType(TokenType.BEARER)
      .expired(false)
      .revoked(false)
      .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(UserEntity userEntity) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(userEntity.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    final String pinCode;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);

    String username = jwtService.extractUsername(refreshToken);
    userEmail = username.substring(0, username.length() - 4);
    pinCode = username.substring(username.length() - 4);
    if (userEmail != null && pinCode != null) {
      var user = this.repository.findByEmailAndPinCode(userEmail, pinCode)
        .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .role(user.getRole())
          .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public Boolean checkLogin(String email, String password) {
    if (password != null) {
      List<UserEntity> byEmailAndPassword = repository.findByEmailAndPassword(
        email, MD5.md5(password)
      );
      return !byEmailAndPassword.isEmpty();
    }

    List<UserEntity> byEmailAndPassword = repository.findByEmail(email);
    return !byEmailAndPassword.isEmpty();
  }
}
