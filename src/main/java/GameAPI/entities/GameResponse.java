package GameAPI.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class GameResponse {

    private static GameResponse instance;

    @JsonIgnore
    Integer bigBlind;
    List<Player> players;

    private GameResponse() {

    }

    public void addGame(Game game) {
        this.bigBlind = game.getBigBlind();
        this.players = game.getPlayers();
    }

    public void clear(){
        this.bigBlind = null;
        this.players = null;
    }

    public static GameResponse getInstance() {
        if(instance == null) {
            instance = new GameResponse();
        }
        return instance;
    }

    public Integer getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(Integer bigBlind) {
        this.bigBlind = bigBlind;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }


}
