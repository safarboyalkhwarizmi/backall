package uz.backall.sellHistory;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class SellHistoryInfoDTO {
    private String name;
    private Long count;
    private Time time;
}
