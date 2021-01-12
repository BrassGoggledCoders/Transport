package xyz.brassgoggledcoders.transport.api.engine;

public enum EngineDirection {
    FORWARD(true),
    NEUTRAL(false),
    REVERSE(true);

    private final boolean moving;

    EngineDirection(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return this.moving;
    }
}
