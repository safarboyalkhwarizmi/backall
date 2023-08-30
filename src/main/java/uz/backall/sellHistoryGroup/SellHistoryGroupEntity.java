package uz.backall.sellHistoryGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.backall.sellGroup.SellGroupEntity;
import uz.backall.sellHistory.SellHistoryEntity;

@Getter
@Setter
@Entity
@Table(name = "sell_history_group")
public class SellHistoryGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sell_group_id")
    private Long sellGroupId;

    @ManyToOne
    @JoinColumn(name = "sell_group_id", insertable = false, updatable = false)
    private SellGroupEntity sellGroup;

    @Column(name = "sell_history_id")
    private Long sellHistoryId;

    @ManyToOne
    @JoinColumn(name = "sell_history_id", insertable = false, updatable = false)
    private SellHistoryEntity sellHistory;
}