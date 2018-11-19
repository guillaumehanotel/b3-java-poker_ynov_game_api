package GameAPI.entities;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TurnBehavior {

    private static TurnBehavior instance;
    private final HashMap<TurnPhase, Method> turnMap;

    private TurnBehavior(){
        this.turnMap = new HashMap<>();
        initBehaviors();
    }

    private void initBehaviors(){
        try {
            turnMap.put(TurnPhase.PREFLOP, Round.class.getMethod("setupPreFlop"));
            turnMap.put(TurnPhase.FLOP, Round.class.getMethod("setupFlop"));
            turnMap.put(TurnPhase.TURN, Round.class.getMethod("setupTurn"));
            turnMap.put(TurnPhase.RIVER, Round.class.getMethod("setupRiver"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Method getInitMethodByTurnPhase(TurnPhase turnPhase){
        return this.turnMap.get(turnPhase);
    }

    public static TurnBehavior getInstance() {
        if(instance == null) {
            instance = new TurnBehavior();
        }
        return instance;
    }

}
