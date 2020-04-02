package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.particles.ParticleTypes;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.engine.EngineInstance;

public class CreativeEngineInstance extends EngineInstance {
    public CreativeEngineInstance(Engine engine, IModularEntity powered) {
        super(engine, powered);
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public double getMaximumSpeed() {
        return 0.3D;
    }

    @Override
    public void tick() {
        super.tick();
        this.handleParticles(ParticleTypes.PORTAL, 4);
    }
}
