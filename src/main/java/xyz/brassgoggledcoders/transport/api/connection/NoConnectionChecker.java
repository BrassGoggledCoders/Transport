package xyz.brassgoggledcoders.transport.api.connection;

import net.minecraft.entity.Entity;

public class NoConnectionChecker implements IConnectionChecker {
    @Override
    public boolean areConnected(Entity one, Entity two) {
        return false;
    }

    @Override
    public Entity getLeader(Entity entity) {
        return null;
    }
}
