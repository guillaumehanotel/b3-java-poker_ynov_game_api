package GameAPI.engine;


public class ResultStat {

    private Integer nbGamePlayed;
    private Integer nbRoundPlayed;
    private Integer nbRoundWon;
    private Integer totalMoneyWon;
    private Integer biggestPotWon;

    public ResultStat() {}

    public ResultStat(Integer nbGamePlayed, Integer nbRoundPlayed, Integer nbRoundWon, Integer moneyWon, Integer biggestPotWon) {
        this.nbGamePlayed = nbGamePlayed;
        this.nbRoundPlayed = nbRoundPlayed;
        this.nbRoundWon = nbRoundWon;
        this.totalMoneyWon = totalMoneyWon;
        this.biggestPotWon = biggestPotWon;
    }

    public Integer getNbGamePlayed() {
        return nbGamePlayed;
    }

    public void setNbGamePlayed(Integer nbGamePlayed) {
        this.nbGamePlayed = nbGamePlayed;
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

    public Integer getTotalMoneyWon() {
        return totalMoneyWon;
    }

    public void setTotalMoneyWon(Integer totalMoneyWon) {
        this.totalMoneyWon = totalMoneyWon;
    }

    public Integer getBiggestPotWon() {
        return biggestPotWon;
    }

    public void setBiggestPotWon(Integer biggestPotWon) {
        this.biggestPotWon = biggestPotWon;
    }
}
