package xyz.brassgoggledcoders.transport.api.connection;

import net.minecraft.entity.Entity;

public interface IConnectionChecker {
    boolean areConnected(Entity one, Entity two);

    Entity getLeader(Entity entity);
}
