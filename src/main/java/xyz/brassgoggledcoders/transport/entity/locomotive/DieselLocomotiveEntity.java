package xyz.brassgoggledcoders.transport.entity.locomotive;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.engine.DieselEngine;

import javax.annotation.Nonnull;

public class DieselLocomotiveEntity extends LocomotiveEntity<DieselEngine> {
    public DieselLocomotiveEntity(EntityType<? extends LocomotiveEntity> type, World world) {
        super(type, world);
    }

    @Override
    public DieselEngine createEngine() {
        return new DieselEngine();
    }

    @Nonnull
    @Override
    public ItemStack createItemStack() {
        return new ItemStack(TransportEntities.DIESEL_LOCOMOTIVE_ITEM.get());
    }
}
