package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.particles.ParticleTypes;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.engine.EngineModuleInstance;

public class CreativeEngineModuleInstance extends EngineModuleInstance {
    public CreativeEngineModuleInstance(EngineModule engineModule, IModularEntity powered) {
        super(engineModule, powered);
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
