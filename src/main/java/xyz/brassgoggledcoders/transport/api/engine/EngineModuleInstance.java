package xyz.brassgoggledcoders.transport.api.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import javax.annotation.Nonnull;

public abstract class EngineModuleInstance extends ModuleInstance<EngineModule> implements IHoldable {
    private PoweredState poweredState = PoweredState.RUNNING;

    private Long lastForced;

    protected EngineModuleInstance(EngineModule engineModule, IModularEntity componentCarrier) {
        super(engineModule, componentCarrier);
    }

    public abstract boolean isRunning();

    public abstract double getMaximumSpeed();

    @Nonnull
    public PoweredState getPoweredState() {
        if (this.poweredState == PoweredState.FORCED_IDLE) {
            if (this.lastForced == null || this.lastForced + 5 < this.getWorld().getGameTime()) {
                this.poweredState = PoweredState.RUNNING;
            }
        }
        return poweredState;
    }

    public void setPoweredState(@Nonnull PoweredState poweredState) {
        this.poweredState = poweredState;
        if (poweredState == PoweredState.FORCED_IDLE) {
            this.lastForced = this.getWorld().getGameTime();
        } else {
            this.lastForced = null;
        }
    }

    protected void handleParticles(IParticleData particleData, int runningAmount) {
        runningAmount *= this.poweredState.getRunningModifier();
        if (this.isRunning() && this.getModularEntity().getTheWorld().rand.nextInt(runningAmount) == 0) {
            this.getModularEntity().getTheWorld().addParticle(particleData, this.getModularEntity().getSelf().getPosX(),
                    this.getModularEntity().getSelf().getPosY() + this.getModularEntity().getSelf().getEyeHeight() + 0.6,
                    this.getModularEntity().getSelf().getPosZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onHeld() {
        this.setPoweredState(PoweredState.FORCED_IDLE);
    }

    @Override
    public void onRelease() {
        this.setPoweredState(PoweredState.RUNNING);
    }

    @Override
    public void push(float xPush, float zPush) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        compoundNBT.putString("poweredState", this.poweredState.name());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.poweredState = PoweredState.byName(nbt.getString("poweredState"));
    }
}
