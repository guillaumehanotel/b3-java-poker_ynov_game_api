package GameAPI.engine.action;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * La classe Action est la représentation d'une action réalisée par un joueur,
 * elle comporte l'ID d'une game, l'ID du joueur, un type d'action et une valeur
 */
@Data
@Slf4j
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

    /**
     * Vérifie que'une action reçue possède bien les champs requis
     */
    public Boolean isValid() {
        boolean check = true;
        if (getGameId() == null) {
            log.error("Missing gameId of Action");
            check = false;
        }
        if (getUserId() == null) {
            log.error("Missing userId of Action");
            check = false;
        }
        if (getActionType() == null) {
            log.error("Missing actionType of Action");
            check = false;
        }
        if (!checkActionTypeParameter()) {
            check = false;
        }
        return check;
    }

    /**
     * Vérifie que'une action possède bien une valeur dans le cas où l'action serait BET
     */
    private Boolean checkActionTypeParameter() {
        boolean check = true;
        if (getActionType() == ActionType.BET) {
            if (getValue() == null) {
                check = false;
                log.error("Missing value for action BET");
            }
        }
        return check;
    }

}
