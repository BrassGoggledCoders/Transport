package xyz.brassgoggledcoders.transport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.tileentity.YardManagerTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class YardManagerBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public YardManagerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new YardManagerTileEntity(TransportBlocks.YARD_MANAGER_TILE_ENTITY.get());
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof YardManagerTileEntity) {
            return ((YardManagerTileEntity) tileEntity).onRightClick(player);
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
