package xyz.brassgoggledcoders.transport.api.engine;

public enum EngineState {
    FORWARD_3(1.5F, 1.0F, EngineDirection.FORWARD),
    FORWARD_2(1.2F, 0.60F, EngineDirection.FORWARD),
    FORWARD_1(1.0F, 0.25F, EngineDirection.FORWARD),
    NEUTRAL(0.5F, 0.0F, EngineDirection.NEUTRAL),
    REVERSE_1(1.0F, 0.3F, EngineDirection.REVERSE);

    private final float fuelUseModifier;
    private final float maxSpeedModifier;
    private final EngineDirection direction;

    EngineState(float fuelUseModifier, float maxSpeedModifier, EngineDirection direction) {
        this.fuelUseModifier = fuelUseModifier;
        this.maxSpeedModifier = maxSpeedModifier;
        this.direction = direction;
    }

    public float getFuelUseModifier() {
        return fuelUseModifier;
    }

    public float getMaxSpeedModifier() {
        return maxSpeedModifier;
    }

    public EngineDirection getDirection() {
        return direction;
    }

    public static EngineState forward(EngineState prior) {
        switch (prior) {
            case FORWARD_3:
                return null;
            case FORWARD_2:
                return FORWARD_3;
            case FORWARD_1:
                return FORWARD_2;
            case NEUTRAL:
                return FORWARD_1;
            case REVERSE_1:
                return NEUTRAL;
        }
        return null;
    }

    public static EngineState reverse(EngineState prior) {
        switch (prior) {
            case FORWARD_3:
                return FORWARD_2;
            case FORWARD_2:
                return FORWARD_1;
            case FORWARD_1:
                return NEUTRAL;
            case NEUTRAL:
                return REVERSE_1;
            case REVERSE_1:
                return null;
        }
        return null;
    }

    public static EngineState byName(String name) {
        for (EngineState state : EngineState.values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return NEUTRAL;
    }
}
