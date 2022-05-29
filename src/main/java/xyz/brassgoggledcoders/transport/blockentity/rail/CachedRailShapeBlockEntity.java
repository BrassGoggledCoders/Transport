package xyz.brassgoggledcoders.transport.blockentity.rail;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CachedRailShapeBlockEntity extends BlockEntity {
    public static final int CACHED_TIME = 10;

    private final Table<UUID, Long, RailShape> railShapes;

    public CachedRailShapeBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.railShapes = HashBasedTable.create();
    }

    public long getLastHitGameTime() {
        return this.railShapes.columnKeySet()
                .stream()
                .max(Long::compareTo)
                .orElse(-1L);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);

    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

    }

    public RailShape getRailShapeFor(AbstractMinecart minecartEntity) {
        long oldestGameTime = minecartEntity.getLevel().getGameTime() - CACHED_TIME;
        return this.railShapes.row(minecartEntity.getUUID())
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .filter(entry -> entry.getKey() > oldestGameTime)
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    public void setRailShapeFor(AbstractMinecart minecartEntity, RailShape railShape) {
        this.railShapes.put(minecartEntity.getUUID(), minecartEntity.getLevel().getGameTime(), railShape);
    }

    public void clean() {
        if (this.getLevel() != null) {
            long oldestGameTime = this.getLevel().getGameTime() - CACHED_TIME;
            this.railShapes.cellSet()
                    .removeIf(cell -> cell.getColumnKey() < oldestGameTime);
        }
    }
}
