package xyz.brassgoggledcoders.transport.engine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;

public abstract class Engine implements INBTSerializable<CompoundNBT> {

    public void tick() {

    }

    public abstract boolean pullPower(EngineState engineState);

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
