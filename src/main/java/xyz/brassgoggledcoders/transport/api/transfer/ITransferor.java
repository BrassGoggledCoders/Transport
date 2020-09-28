package xyz.brassgoggledcoders.transport.api.transfer;

import net.minecraftforge.common.capabilities.Capability;

public interface ITransferor<T> {
    Capability<T> getCapability();

    boolean transfer(T from, T to, int transferAmount);

    default boolean transfer(T from, T to) {
        return transfer(from, to, this.getDefaultAmount());
    }

    int getDefaultAmount();
}
