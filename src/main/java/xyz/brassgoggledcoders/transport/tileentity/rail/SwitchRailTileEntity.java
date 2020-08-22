package xyz.brassgoggledcoders.transport.tileentity.rail;

import com.google.common.collect.Maps;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class SwitchRailTileEntity extends TileEntity {
    private final Map<UUID, Pair<Long, RailShape>> cachedRailShapes;

    public SwitchRailTileEntity() {
        this(TransportBlocks.SWITCH_RAIL_TILE_ENTITY.get());
    }

    public SwitchRailTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.cachedRailShapes = Maps.newHashMap();
    }

    @Nullable
    public RailShape getCachedRailShape(AbstractMinecartEntity minecartEntity) {
        long gameTime = this.getGameTime();
        cachedRailShapes.entrySet().removeIf(entry -> entry.getValue().getLeft() + 5 < gameTime);

        Pair<Long, RailShape> cachedRailShape = cachedRailShapes.get(minecartEntity.getUniqueID());
        if (cachedRailShape != null) {
            cachedRailShapes.put(minecartEntity.getUniqueID(), Pair.of(gameTime, cachedRailShape.getRight()));
            return cachedRailShape.getRight();
        } else {
            return null;
        }
    }

    public void setCachedRailShape(AbstractMinecartEntity minecartEntity, RailShape railShape) {
        cachedRailShapes.put(minecartEntity.getUniqueID(), Pair.of(this.getGameTime(), railShape));
    }

    public long getGameTime() {
        if (this.getWorld() != null) {
            return this.getWorld().getGameTime();
        } else {
            return 0;
        }
    }
}
