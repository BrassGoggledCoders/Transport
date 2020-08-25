package xyz.brassgoggledcoders.transport.api.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

import javax.annotation.Nonnull;

public abstract class EngineModuleInstance extends ModuleInstance<EngineModule> implements IHoldable {
    private PoweredState poweredState = PoweredState.RUNNING;

    protected EngineModuleInstance(EngineModule engineModule, IModularEntity componentCarrier) {
        super(engineModule, componentCarrier);
    }

    public abstract boolean isRunning();

    public abstract double getMaximumSpeed();

    @Nonnull
    public PoweredState getPoweredState() {
        return poweredState;
    }

    public void setPoweredState(@Nonnull PoweredState poweredState) {
        this.poweredState = poweredState;
    }

    protected void handleParticles(IParticleData particleData, int runningAmount) {
        if (this.getPoweredState() == PoweredState.IDLE) {
            runningAmount *= 2;
        }
        if (this.isRunning() && this.getModularEntity().getTheWorld().rand.nextInt(runningAmount) == 0) {
            this.getModularEntity().getTheWorld().addParticle(particleData, this.getModularEntity().getSelf().getPosX(),
                    this.getModularEntity().getSelf().getPosY() + this.getModularEntity().getSelf().getEyeHeight() + 0.6,
                    this.getModularEntity().getSelf().getPosZ(), 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    public void onHeld() {
        this.setPoweredState(PoweredState.IDLE);
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
