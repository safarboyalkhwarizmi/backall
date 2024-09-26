package uz.backall.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService service;

  @Operation(summary = "Register a new user",
    description = "Registers a new user by accepting their personal details such as firstname, lastname, store name, email, password, and pinCode. Role of the user should also be specified.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User registered successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
    @ApiResponse(responseCode = "400", description = "Invalid input data",
      content = @Content)
  })
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @RequestHeader(name = "Idempotency-Key") String idempotencyKey,
    @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(idempotencyKey, request));
  }

  @Operation(summary = "Authenticate user",
    description = "Authenticates a user by verifying the provided email, password, and pinCode. Returns an authentication token if credentials are valid.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Authentication successful",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials",
      content = @Content)
  })
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
    @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @Operation(summary = "Check user authentication",
    description = "Verifies if the provided email and password are valid without returning an authentication token.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Check completed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))}),
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials",
      content = @Content)
  })
  @GetMapping("/check")
  public ResponseEntity<Boolean> checkLogin(
    @RequestBody AuthenticationCheckRequest request
  ) {
    return ResponseEntity.ok(service.checkLogin(request));
  }

  @Operation(summary = "Check user email",
    description = "Verifies if the provided email are valid without returning an authentication token.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Check completed successfully",
      content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))}),
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials",
      content = @Content)
  })
  @GetMapping("/check/email")
  public ResponseEntity<Boolean> checkEmail(
    @RequestParam String email
  ) {
    return ResponseEntity.ok(service.checkEmail(email));
  }

  @Operation(summary = "Refresh authentication token",
    description = "Refreshes the user's authentication token using the current refresh token. This endpoint does not require a request body.")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
      content = @Content),
    @ApiResponse(responseCode = "401", description = "Unauthorized, invalid refresh token",
      content = @Content)
  })
  @PostMapping("/refresh-token")
  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}