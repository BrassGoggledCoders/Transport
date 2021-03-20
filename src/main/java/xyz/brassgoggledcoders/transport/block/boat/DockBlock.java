package xyz.brassgoggledcoders.transport.block.boat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.WayStationTileEntity;
import xyz.brassgoggledcoders.transport.tileentity.boat.DockTileEntity;

import javax.annotation.Nullable;

public class DockBlock extends WayStationBlock {
    public DockBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DockTileEntity(TransportBlocks.DOCK_TILE_ENTITY.get());
    }
}
