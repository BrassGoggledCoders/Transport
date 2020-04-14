package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.tileentity.ModuleConfiguratorTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

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

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof ModuleConfiguratorTileEntity) {
            return ((ModuleConfiguratorTileEntity) tileEntity).openScreen(player);
        }
        return ActionResultType.PASS;
    }
}
