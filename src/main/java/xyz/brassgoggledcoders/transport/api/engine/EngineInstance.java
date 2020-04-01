package xyz.brassgoggledcoders.transport.api.engine;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.Vec3d;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;

import javax.annotation.Nonnull;

public abstract class EngineInstance extends ModuleInstance<Engine> {
    private PoweredState poweredState = PoweredState.IDLE;

    protected EngineInstance(Engine engine, IModularEntity componentCarrier) {
        super(engine, componentCarrier);
    }

    public abstract boolean isRunning();

    public abstract double getMaximumSpeed();

    @Nonnull
    public PoweredState getPoweredState() {
        return poweredState;
    }

    public void setPoweredState(@Nonnull PoweredState poweredState, @Nonnull Vec3d push) {
        this.poweredState = poweredState;
    }

    protected void handleParticles(IParticleData particleData, int runningAmount) {
        if (this.getPoweredState() == PoweredState.IDLE) {
            runningAmount *= 2;
        }
        if (this.isRunning() && this.getModularEntity().getTheWorld().rand.nextInt(runningAmount) == 0) {
            this.getModularEntity().getTheWorld().addParticle(particleData, this.getModularEntity().getSelf().getPosX(),
                    this.getModularEntity().getSelf().getPosY() + 0.8D, this.getModularEntity().getSelf().getPosZ(),
                    0.0D, 0.0D, 0.0D);
        }

    }
}
