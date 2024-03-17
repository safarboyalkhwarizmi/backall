package uz.backall;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.backall.auth.UserAlreadyExistsException;
import uz.backall.config.jwt.JwtParseException;
import uz.backall.config.jwt.JwtUsernameException;
import uz.backall.products.ProductNotFoundException;
import uz.backall.sell.sellHistory.SellingPriceException;
import uz.backall.store.StoreAlreadyExistsException;
import uz.backall.store.StoreNotFoundException;


import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("status", status.value());

    List<String> errors = new LinkedList<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.add(fieldError.getDefaultMessage());
    }
    body.put("errors", errors);
    return new ResponseEntity<>(body, headers, status);
  }

  @ExceptionHandler({JwtParseException.class})
  private ResponseEntity<?> handler(JwtParseException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  @ExceptionHandler({JwtUsernameException.class})
  private ResponseEntity<?> handler(JwtUsernameException e) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
  }

  @ExceptionHandler({StoreAlreadyExistsException.class})
  private ResponseEntity<?> handler(StoreAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler({UserAlreadyExistsException.class})
  private ResponseEntity<?> handler(UserAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler({ProductNotFoundException.class})
  private ResponseEntity<?> handler(ProductNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler({SellingPriceException.class})
  private ResponseEntity<?> handler(SellingPriceException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler({StoreNotFoundException.class})
  private ResponseEntity<?> handler(StoreNotFoundException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
}