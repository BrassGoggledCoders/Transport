package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class DieselLocomotiveEntity extends LocomotiveEntity {
    public DieselLocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
    }
}
