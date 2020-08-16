package xyz.brassgoggledcoders.transport.quark.connection;

import net.minecraft.entity.Entity;
import vazkii.quark.automation.base.ChainHandler;
import xyz.brassgoggledcoders.transport.api.connection.IConnectionChecker;

public class QuarkConnectionChecker implements IConnectionChecker {
    @Override
    public boolean areConnected(Entity one, Entity two) {
        return ChainHandler.getLinked(one) == two || ChainHandler.getLinked(two) == one;
    }

    @Override
    public Entity getLeader(Entity entity) {
        Entity leader = ChainHandler.getLinked(entity);
        if (leader != null) {
            Entity nextLeader = ChainHandler.getLinked(entity);
            while (nextLeader != null) {
                leader = nextLeader;
                nextLeader = ChainHandler.getLinked(nextLeader);
            }
        }
        return leader;
    }
}
