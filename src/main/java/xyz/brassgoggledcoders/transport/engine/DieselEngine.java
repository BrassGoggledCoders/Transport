package xyz.brassgoggledcoders.transport.engine;

import xyz.brassgoggledcoders.transport.api.engine.EngineState;

public class DieselEngine extends Engine {
    @Override
    public boolean pullPower(EngineState engineState) {
        return false;
    }
}
