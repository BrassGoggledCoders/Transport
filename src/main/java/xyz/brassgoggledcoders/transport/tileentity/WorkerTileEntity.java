package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class WorkerTileEntity extends TileEntity {
    public WorkerTileEntity(TileEntityType<? extends WorkerTileEntity> tileEntityType) {
        super(tileEntityType);
    }
}
