package uz.backall.store;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.user.User;

@Getter
@Setter
@Entity
@Table(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}