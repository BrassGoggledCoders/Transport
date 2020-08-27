package xyz.brassgoggledcoders.transport.api.predicate;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PredicateStorageProvider implements ICapabilityProvider {
    private final LazyOptional<PredicateStorage> routingStorageLazyOptional;

    public PredicateStorageProvider() {
        PredicateStorage predicateStorage = new PredicateStorage();
        this.routingStorageLazyOptional = LazyOptional.of(() -> predicateStorage);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return TransportAPI.PREDICATE_STORAGE.orEmpty(cap, routingStorageLazyOptional);
    }
}
