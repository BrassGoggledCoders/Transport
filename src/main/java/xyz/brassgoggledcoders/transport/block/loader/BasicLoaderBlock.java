package xyz.brassgoggledcoders.transport.block.loader;

import com.teamacronymcoders.base.blocks.BlockSidedBase;
import net.minecraft.block.material.Material;
import xyz.brassgoggledcoders.transport.tileentity.loader.TileEntityLoaderBase;

public abstract class BasicLoaderBlock<T extends TileEntityLoaderBase> extends BlockSidedBase<T> {
    protected BasicLoaderBlock(String name) {
        super(Material.IRON, name + "_loader");
    }
}
