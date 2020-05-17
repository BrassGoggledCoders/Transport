package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.particles.RedstoneParticleData;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModuleInstance;

public class BoosterEngineModuleInstance extends EngineModuleInstance {
    public BoosterEngineModuleInstance(EngineModule engineModule, IModularEntity powered) {
        super(engineModule, powered);
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public double getMaximumSpeed() {
        return 0.05D;
    }

    @Override
    public void tick() {
        super.tick();
        this.handleParticles(RedstoneParticleData.REDSTONE_DUST, 4);
    }
}
