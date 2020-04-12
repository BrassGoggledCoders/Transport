package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;

import javax.annotation.Nullable;

public class ModuleConfiguratorBlock extends Block {
    public ModuleConfiguratorBlock() {
        this(Properties.create(Material.IRON));
    }

    public ModuleConfiguratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState blockState) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ModuleConfiguratorTileEntity();
    }
}
