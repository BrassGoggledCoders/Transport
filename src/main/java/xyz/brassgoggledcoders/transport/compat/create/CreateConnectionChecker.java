package xyz.brassgoggledcoders.transport.compat.create;

import com.google.common.collect.Sets;
import com.simibubi.create.content.contraptions.components.structureMovement.train.capability.MinecartController;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import vazkii.quark.content.automation.base.ChainHandler;
import xyz.brassgoggledcoders.transport.api.connection.IConnectionChecker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class CreateConnectionChecker implements IConnectionChecker {
    @CapabilityInject(MinecartController.class)
    private static Capability<MinecartController> MINECART_CONTROLLER;

    @Override
    public boolean areConnected(@Nonnull Entity one, @Nonnull Entity two) {
        if (MINECART_CONTROLLER != null) {
            return one.getCapability(MINECART_CONTROLLER)
                    .map(minecartController ->
                            two.getUniqueID().equals(minecartController.getCoupledCart(true)) ||
                                    two.getUniqueID().equals(minecartController.getCoupledCart(false))
                    )
                    .orElse(false);
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Entity getLeader(@Nullable Entity entity) {
        if (MINECART_CONTROLLER != null && entity != null) {
            Entity leader = getNextLeader(entity);
            if (leader != null) {
                Set<Entity> checked = Sets.newHashSet(entity, leader);
                Entity nextLeader = getNextLeader(entity);
                while (nextLeader != null && checked.add(nextLeader)) {
                    leader = nextLeader;
                    nextLeader = getNextLeader(nextLeader);
                }
            }
            return leader;
        } else {
            return null;
        }
    }

    @Nullable
    private Entity getNextLeader(@Nonnull Entity entity) {
        if (entity.getEntityWorld() instanceof ServerWorld) {
            return entity.getCapability(MINECART_CONTROLLER)
                    .resolve()
                    .map(minecartController -> minecartController.getCoupledCart(true))
                    .map(uuid -> ((ServerWorld) entity.getEntityWorld()).getEntityByUuid(uuid))
                    .orElse(null);
        } else {
            return null;
        }
    }
}
