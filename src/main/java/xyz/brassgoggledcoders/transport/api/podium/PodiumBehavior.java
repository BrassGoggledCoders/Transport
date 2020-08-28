package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Predicate;

public class PodiumBehavior implements INBTSerializable<CompoundNBT>, Predicate<Entity> {
    private final IPodium podium;

    public PodiumBehavior(IPodium podium) {
        this.podium = podium;
    }

    public int getComparatorSignal() {
        return -1;
    }

    public IPodium getPodium() {
        return podium;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public boolean test(Entity entity) {
        return false;
    }
}
