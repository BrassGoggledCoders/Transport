package xyz.brassgoggledcoders.transport.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

public interface IModularItem<T extends Entity & IModularEntity> {
    EntityType<T> getEntityType();
}
