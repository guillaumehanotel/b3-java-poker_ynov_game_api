package GameAPI.entities;

import lombok.Data;

/**
 * La classe Action est la représentation d'une action réalisée par un joueur,
 * elle comporte l'ID d'une game, l'ID du joueur, un type d'action et une valeur
 */
@Data
public class Action {

    private Integer gameId;
    private Integer userId;
    private ActionType actionType;
    private Integer value;


    public Integer getGameId() {
        return gameId;
    }

    public Integer getUserId() {
        return userId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Action{" +
                "gameId=" + gameId +
                ", userId=" + userId +
                ", actionType=" + actionType +
                ", value=" + value +
                '}';
    }
}
