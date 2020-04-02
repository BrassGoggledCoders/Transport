package xyz.brassgoggledcoders.transport.api.engine;

public enum PoweredState {
    RUNNING(true),
    IDLE(false),
    NO_FUEL(false);

    private final boolean moving;

    PoweredState(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }
}
