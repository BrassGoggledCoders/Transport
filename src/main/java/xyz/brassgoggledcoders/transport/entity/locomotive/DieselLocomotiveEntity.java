package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.engine.DieselEngine;

public class DieselLocomotiveEntity extends LocomotiveEntity<DieselEngine> {
    public DieselLocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
    }

    @Override
    public DieselEngine createEngine() {
        return new DieselEngine();
    }
}
