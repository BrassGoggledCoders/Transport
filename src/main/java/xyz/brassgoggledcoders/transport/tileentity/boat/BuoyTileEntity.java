package xyz.brassgoggledcoders.transport.tileentity.boat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.UUID;

public class BuoyTileEntity extends TileEntity {
    private UUID navigationPointUniqueId;

    public BuoyTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void interact(PlayerEntity playerEntity) {
        if (navigationPointUniqueId == null) {
            create(playerEntity);
        }
    }

    public void setup(@Nullable PlayerEntity playerEntity) {
        create(playerEntity);
    }

    public void destroy() {

    }

    protected void create(@Nullable PlayerEntity playerEntity) {
        if (this.getWorld() != null) {

        }
    }
}
