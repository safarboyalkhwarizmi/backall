package uz.backall.auth;

import uz.backall.config.jwt.JwtService;
import uz.backall.store.StoreService;
import uz.backall.token.Token;
import uz.backall.token.TokenRepository;
import uz.backall.token.TokenType;
import uz.backall.user.Role;
import uz.backall.user.User;
import uz.backall.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.backall.util.MD5;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final StoreService storeService;

  public AuthenticationResponse register(RegisterRequest request, User owner) {
    if (
      !request.getRole().equals(Role.ADMIN) &&
      owner.getRole().equals(Role.ADMIN)
    ) {
      throw new RegisterNotAllowedException("Registering is not allowed.");
    }

    Optional<User> byEmail = repository.findByEmailAndPinCode(
      request.getEmail(),
      request.getPinCode()
    );
    if (byEmail.isPresent()) {
      throw new UserAlreadyExistsException("User already exists.");
    }

    var user = User.builder()
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .email(request.getEmail())
      .password(MD5.md5(request.getPassword()))
      .pinCode(request.getPinCode())
      .role(request.getRole())
      .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    Long storeId;
    if (request.getRole().equals(Role.BOSS)) {
      storeId = storeService.create(savedUser.getId(), request.getStoreName());
    } else {
      Optional<User> byEmailAndRole = repository.findByEmailAndRole(savedUser.getEmail(), Role.BOSS);
      if (byEmailAndRole.isEmpty()) {
        throw new BossNotFoundException("Boss not found");
      }

      User bossProfile = byEmailAndRole.get();
      storeId = storeService.getStoresByUserId(bossProfile.getId()).get(0).getId();
    }


    return AuthenticationResponse.builder()
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .role(request.getRole())
      .storeId(storeId)
      .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    List<User> byEmailAndPassword = repository.findByEmailAndPassword(
      request.getEmail(), MD5.md5(request.getPassword())
    );
    if (byEmailAndPassword.isEmpty()) {
      throw new UserNotFoundException("ACCOUNT NOT FOUND!");
    }

    var user = repository.findByEmailAndPinCode(request.getEmail(), request.getPinCode())
      .orElseThrow();

    Optional<User> byEmailAndRole = repository.findByEmailAndRole(user.getEmail(), Role.BOSS);
    if (byEmailAndRole.isEmpty()) {
      throw new BossNotFoundException("Boss not found");
    }

    User bossProfile = byEmailAndRole.get();
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

  public Boolean check(AuthenticationCheckRequest request) {

    System.out.println(MD5.md5(request.getPassword()));
    List<User> byEmailAndPassword = repository.findByEmailAndPassword(
      request.getEmail(), MD5.md5(request.getPassword())
    );
    return !byEmailAndPassword.isEmpty();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
      .user(user)
      .token(jwtToken)
      .tokenType(TokenType.BEARER)
      .expired(false)
      .revoked(false)
      .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
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
}
