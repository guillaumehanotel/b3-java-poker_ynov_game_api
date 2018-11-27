package GameAPI.engine.user;

import GameAPI.engine.game.Result;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class User {

    private Integer id;
    private String email;
    private String username;
    private Integer money;
    Result result;

    public User(String email, String username, Integer money) {
        this.email = email;
        this.username = username;
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }


    public Integer getNbGamesPlayed() {
        return result.getNbGamesPlayed();
    }

    public Integer getNbGamesWon() {
        return result.getNbGamesWon();
    }

    public Integer getNbRoundPlayed() {
        return result.getNbRoundPlayed();
    }

    public Integer getNbRoundWon() {
        return result.getNbRoundWon();
    }

    public Integer getTotalEarnedMoney() {
        return result.getTotalEarnedMoney();
    }

    public Integer getBiggestPotWon() {
        return result.getBiggestPotWon();
    }

    public void incrementNbGamesPlayed() {
        result.incrementNbGamesPlayed();
    }

    public void incrementNbGamesWon() {
        result.incrementNbGamesWon();
    }

    public void incrementNbRoundPlayed() {
        result.incrementNbRoundPlayed();
    }

    public void incrementNbRoundWon() {
        result.incrementNbRoundWon();
    }

    public void incrementTotalEarnedMoney(Integer totalEarnedMoney) {
        result.incrementTotalEarnedMoney(totalEarnedMoney);
    }

    public void setBiggestPotWon(Integer biggestPotWon) {
        result.setBiggestPotWon(biggestPotWon);
    }

}


