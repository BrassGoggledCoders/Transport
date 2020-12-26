package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class SteamLocomotiveEntity extends LocomotiveEntity {
    public SteamLocomotiveEntity(EntityType<? extends SteamLocomotiveEntity> type, World world) {
        super(type, world);
    }
}
