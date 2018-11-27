package GameAPI.engine.game;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Paris")
    private Date date;
    private String combinaison;

}
