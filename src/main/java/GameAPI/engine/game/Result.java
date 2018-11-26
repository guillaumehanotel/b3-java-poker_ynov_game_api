package GameAPI.engine.game;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNbGamesPlayed() {
        return nbGamesPlayed;
    }

    public void setNbGamesPlayed(Integer nbGamesPlayed) {
        this.nbGamesPlayed = nbGamesPlayed;
    }

    public Integer getNbGamesWon() {
        return nbGamesWon;
    }

    public void setNbGamesWon(Integer nbGamesWon) {
        this.nbGamesWon = nbGamesWon;
    }

    public Integer getNbRoundPlayed() {
        return nbRoundPlayed;
    }

    public void setNbRoundPlayed(Integer nbRoundPlayed) {
        this.nbRoundPlayed = nbRoundPlayed;
    }

    public Integer getNbRoundWon() {
        return nbRoundWon;
    }

    public void setNbRoundWon(Integer nbRoundWon) {
        this.nbRoundWon = nbRoundWon;
    }

    public Integer getTotalEarnedMoney() {
        return totalEarnedMoney;
    }

    public void setTotalEarnedMoney(Integer totalEarnedMoney) {
        this.totalEarnedMoney = totalEarnedMoney;
    }

    public Integer getBiggestPotWon() {
        return biggestPotWon;
    }

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
