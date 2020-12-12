package xyz.brassgoggledcoders.transport.api.connection;

import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IConnectionChecker {
    boolean areConnected(@Nonnull Entity one, @Nonnull Entity two);

    @Nullable
    Entity getLeader(@Nullable Entity entity);
}
