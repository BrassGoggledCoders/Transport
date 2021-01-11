package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportBlockStateProperties;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.util.RailUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BumperRailBlock extends AbstractRailBlock {
    public static final BooleanProperty NORTH_WEST = TransportBlockStateProperties.NORTH_WEST;
    public static final EnumProperty<RailShape> SHAPE = TransportBlockStateProperties.STRAIGHT_RAIL_SHAPE;

    public BumperRailBlock() {
        this(Block.Properties.create(Material.MISCELLANEOUS)
                .notSolid()
                .hardnessAndResistance(1.0F));
    }

    public BumperRailBlock(Properties properties) {
        super(true, properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH_WEST, SHAPE);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return RailUtils.setRailStateWithFacing(this.getDefaultState(), context);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult rayTraceResult) {
        if (TransportItemTags.WRENCHES.contains(player.getHeldItem(hand).getItem())) {
            state = state.with(NORTH_WEST, !state.get(NORTH_WEST));
            world.setBlockState(pos, state, 3);
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
    }

    @Override
    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}
