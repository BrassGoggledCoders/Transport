package xyz.brassgoggledcoders.transport.api.engine;

public enum PoweredState {
    RUNNING(2, 1),
    FORCED_IDLE(1, 2),
    IDLE(1, 2);

    private final int burnAmount;
    private final int runningModifier;

    PoweredState(int burnAmount, int runningModifier) {
        this.burnAmount = burnAmount;
        this.runningModifier = runningModifier;
    }

    public static PoweredState byName(String name) {
        for (PoweredState state: PoweredState.values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return IDLE;
    }

    public int getBurnAmount() {
        return burnAmount;
    }

    public int getRunningModifier() {
        return runningModifier;
    }
}
