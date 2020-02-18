package xyz.brassgoggledcoders.transport.block.loader;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.transport.tileentity.loader.ItemLoaderTileEntity;

import javax.annotation.Nullable;

public class ItemLoaderBlock extends BasicLoaderBlock {
    public ItemLoaderBlock() {
        this(Properties.create(Material.IRON, MaterialColor.IRON)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL));
    }

    public ItemLoaderBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ItemLoaderTileEntity();
    }
}
