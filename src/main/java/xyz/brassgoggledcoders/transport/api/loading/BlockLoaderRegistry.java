package xyz.brassgoggledcoders.transport.api.loading;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Map;

public class BlockLoaderRegistry {
    private final Multimap<EntityType<?>, IEntityBlockLoading> entityBlockLoadingMap;
    private final Multimap<Block, IBlockEntityLoading> blockEntityLoadingMap;

    public BlockLoaderRegistry() {
        this.entityBlockLoadingMap = Multimaps.newMultimap(Maps.newHashMap(), Lists::newArrayList);
        this.blockEntityLoadingMap = Multimaps.newMultimap(Maps.newHashMap(), Lists::newArrayList);
    }

    public Multimap<EntityType<?>, IEntityBlockLoading> getEntityBlockLoadingMap() {
        return entityBlockLoadingMap;
    }

    @Nonnull
    public Collection<IEntityBlockLoading> getBlockLoadingFor(Entity entity) {
        return this.getEntityBlockLoadingMap().get(entity.getType());
    }

    @ParametersAreNonnullByDefault
    public void registerBlockLoadingFor(EntityType<?> entityType, IEntityBlockLoading entityBlockLoading) {
        this.getEntityBlockLoadingMap().put(entityType, entityBlockLoading);
    }

    public Multimap<Block, IBlockEntityLoading>  getBlockEntityLoadingMap() {
        return blockEntityLoadingMap;
    }

    @Nonnull
    public Collection<IBlockEntityLoading> getEntityLoadingFor(Block block) {
        return this.getBlockEntityLoadingMap().get(block);
    }

    @ParametersAreNonnullByDefault
    public void registerEntityLoadingFor(Block block, IBlockEntityLoading blockEntityLoading) {
        this.getBlockEntityLoadingMap().put(block, blockEntityLoading);
    }
}
