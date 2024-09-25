package uz.backall.idempotencyKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "idempotency_key")
public class IdempotencyKeyEntity {
  @Id
  private String key;

  @Column(columnDefinition="text", length=10485760)
  private String response;

  private LocalDateTime expiryDate;
}