package xyz.brassgoggledcoders.transport.api.engine;

public enum PoweredState {
    RUNNING, IDLE;

    public static PoweredState byName(String name) {
        for (PoweredState state: PoweredState.values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return IDLE;
    }
}
