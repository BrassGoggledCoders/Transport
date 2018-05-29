package xyz.brassgoggledcoders.transport.library.block.loader;

import com.teamacronymcoders.base.blocks.BlockSidedBase;
import net.minecraft.block.material.Material;
import xyz.brassgoggledcoders.transport.library.tileentity.loader.TileEntityLoaderBase;

public abstract class BlockLoaderBase<T extends TileEntityLoaderBase> extends BlockSidedBase<T> {
    protected BlockLoaderBase(String name) {
        super(Material.IRON, name + "_loader");
    }
}
