package uz.backall.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.backall.user.User;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return (User) authentication.getPrincipal();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
    @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/check")
  public ResponseEntity<Boolean> authenticate(
    @RequestBody AuthenticationCheckRequest request
  ) {
    return ResponseEntity.ok(service.check(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}