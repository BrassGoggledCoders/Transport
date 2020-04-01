package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.particles.RedstoneParticleData;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.engine.EngineInstance;

public class BoosterEngineInstance extends EngineInstance {
    public BoosterEngineInstance(Engine engine, IModularEntity powered) {
        super(engine, powered);
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
