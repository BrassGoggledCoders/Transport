package xyz.brassgoggledcoders.transport.api.transfer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public interface ITransferor<T> {
    Capability<T> getCapability();

    boolean transfer(T from, T to, int transferAmount);

    default boolean transfer(T from, T to) {
        return transfer(from, to, this.getDefaultAmount());
    }

    default boolean transfer(LazyOptional<T> from, LazyOptional<T> to) {
        return from.map(fromValue ->
                to.map(toValue ->
                        transfer(fromValue, toValue)
                ).orElse(false)
        ).orElse(false);
    }

    default boolean transfer(ICapabilityProvider from, ICapabilityProvider to) {
        return transfer(from.getCapability(this.getCapability()), to.getCapability(this.getCapability()));
    }

    int getDefaultAmount();
}
