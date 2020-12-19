package xyz.brassgoggledcoders.transport.api.loading;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class BlockLoaderRegistry {
    private final Map<EntityType<?>, IEntityBlockLoading> entityBlockLoadingMap;

    public BlockLoaderRegistry() {
        this.entityBlockLoadingMap = Maps.newHashMap();
    }

    public Map<EntityType<?>, IEntityBlockLoading> getEntityBlockLoadingMap() {
        return entityBlockLoadingMap;
    }

    @Nullable
    public IEntityBlockLoading getBlockLoadingFor(Entity entity) {
        return this.getEntityBlockLoadingMap().get(entity.getType());
    }

    @ParametersAreNonnullByDefault
    public void registerBlockLoadingFor(EntityType<?> entityType, IEntityBlockLoading entityBlockLoading) {
        this.getEntityBlockLoadingMap().put(entityType, entityBlockLoading);
    }
}
