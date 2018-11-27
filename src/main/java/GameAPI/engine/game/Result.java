package GameAPI.engine.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result {

    private Integer userId;
    private Integer gameId;
    private Integer moneyWon = 0;
    private Date date;
    private String combinaison;

}
