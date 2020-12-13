package xyz.brassgoggledcoders.transport.compat.quark;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import vazkii.quark.automation.base.ChainHandler;
import xyz.brassgoggledcoders.transport.api.connection.IConnectionChecker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class QuarkConnectionChecker implements IConnectionChecker {
    @Override
    public boolean areConnected(@Nonnull Entity one, @Nonnull Entity two) {
        return ChainHandler.getLinked(one) == two || ChainHandler.getLinked(two) == one;
    }

    @Override
    @Nullable
    public Entity getLeader(@Nullable Entity entity) {
        Entity leader = ChainHandler.getLinked(entity);
        if (leader != null) {
            Set<Entity> checked = Sets.newHashSet(entity, leader);
            Entity nextLeader = ChainHandler.getLinked(entity);
            while (nextLeader != null && checked.add(nextLeader)) {
                leader = nextLeader;
                nextLeader = ChainHandler.getLinked(nextLeader);
            }
        }
        return leader;
    }
}
