package GameAPI.engine.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result {

    private Integer userId;
    private Integer nbGamesPlayed;
    private Integer nbGamesWon;
    private Integer nbRoundPlayed;
    private Integer nbRoundWon;
    private Integer totalEarnedMoney;
    private Integer biggestPotWon;

    public Result(Integer userId) {
        this.userId = userId;
    }

    public Result(Integer nbGamesPlayed, Integer nbGamesWon, Integer nbRoundPlayed, Integer nbRoundWon, Integer totalEarnedMoney, Integer biggestPotWon) {
        this.nbGamesPlayed = nbGamesPlayed;
        this.nbGamesWon = nbGamesWon;
        this.nbRoundPlayed = nbRoundPlayed;
        this.nbRoundWon = nbRoundWon;
        this.totalEarnedMoney = totalEarnedMoney;
        this.biggestPotWon = biggestPotWon;
    }

    public void incrementNbGamesPlayed() {
        this.nbGamesPlayed++;
    }

    public void incrementNbGamesWon() {
        this.nbGamesWon++;
    }

    public void incrementNbRoundPlayed() {
        this.nbRoundPlayed++;
    }

    public void incrementNbRoundWon() { this.nbRoundWon++; }

    public void incrementTotalEarnedMoney(Integer totalEarnedMoney) { this.totalEarnedMoney += totalEarnedMoney; }

    public void setBiggestPotWon(Integer biggestPotWon) {
        this.biggestPotWon = biggestPotWon;
    }


    @Override
    public String toString() {
        return "Result{" +
                "userId=" + userId +
                ", nbGamesPlayed=" + nbGamesPlayed +
                ", nbGamesWon=" + nbGamesWon +
                ", nbRoundPlayed=" + nbRoundPlayed +
                ", nbRoundWon=" + nbRoundWon +
                ", totalEarnedMoney=" + totalEarnedMoney +
                ", biggestPotWon=" + biggestPotWon +
                '}';
    }
}
