package GameAPI.Game.engine.game;

public enum TurnPhase {

    PREFLOP,
    FLOP,
    TURN,
    RIVER;

    private static final TurnPhase[] values = values();

    public TurnPhase getNextPhase() {
        return values[(this.ordinal() + 1) % values.length];
    }

}
