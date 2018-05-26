package xyz.brassgoggledcoders.transport.loaders.block;

import com.teamacronymcoders.base.blocks.BlockSidedBase;
import net.minecraft.block.material.Material;
import xyz.brassgoggledcoders.transport.loaders.tileentity.TileEntityLoaderBase;

public abstract class BlockLoaderBase<T extends TileEntityLoaderBase> extends BlockSidedBase<T> {
    protected BlockLoaderBase(String name) {
        super(Material.IRON, name + "_loader");
    }
}
