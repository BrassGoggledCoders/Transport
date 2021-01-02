package xyz.brassgoggledcoders.transport.api.engine;

import javax.annotation.Nullable;

public enum EngineState {
    FORWARD_3(1.25F, 1.00F, EngineDirection.FORWARD),
    FORWARD_2(1.00F, 0.60F, EngineDirection.FORWARD),
    FORWARD_1(0.75F, 0.25F, EngineDirection.FORWARD),
    NEUTRAL_0(0.50F, 0.00F, EngineDirection.NEUTRAL),
    REVERSE_1(0.75F, 0.25F, EngineDirection.REVERSE),
    REVERSE_2(1.00F, 0.60F, EngineDirection.REVERSE),
    REVERSE_3(1.25F, 1.00F, EngineDirection.REVERSE);

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

    @Nullable
    public static EngineState forward(EngineState prior) {
        switch (prior) {
            case FORWARD_3:
                return null;
            case FORWARD_2:
                return FORWARD_3;
            case FORWARD_1:
                return FORWARD_2;
            case NEUTRAL_0:
                return FORWARD_1;
            case REVERSE_1:
                return NEUTRAL_0;
            case REVERSE_2:
                return REVERSE_1;
            case REVERSE_3:
                return REVERSE_2;
        }
        return null;
    }

    @Nullable
    public static EngineState reverse(EngineState prior) {
        switch (prior) {
            case FORWARD_3:
                return FORWARD_2;
            case FORWARD_2:
                return FORWARD_1;
            case FORWARD_1:
                return NEUTRAL_0;
            case NEUTRAL_0:
                return REVERSE_1;
            case REVERSE_1:
                return REVERSE_2;
            case REVERSE_2:
                return REVERSE_3;
            case REVERSE_3:
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
        return NEUTRAL_0;
    }
}
