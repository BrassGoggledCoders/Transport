package xyz.brassgoggledcoders.transport.container;

import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.entity.EntityWorldPosCallable;

import java.util.Optional;

public class EntityLocatorInstance extends LocatorInstance {
    public int entityId;

    public EntityLocatorInstance() {
        super(Transport.ENTITY);
    }

    public EntityLocatorInstance(Entity entity) {
        super(Transport.ENTITY);
        this.entityId = entity.getEntityId();
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.ofNullable(playerEntity.world.getEntityByID(entityId));
    }

    @Override
    public IWorldPosCallable getWorldPosCallable(World world) {
        return Optional.ofNullable(world.getEntityByID(entityId))
                .map(entity -> ((IWorldPosCallable)new EntityWorldPosCallable(entity)))
                .orElse(IWorldPosCallable.DUMMY);
    }
}
