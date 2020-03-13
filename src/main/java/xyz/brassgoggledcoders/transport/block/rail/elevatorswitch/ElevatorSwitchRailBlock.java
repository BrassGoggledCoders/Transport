package xyz.brassgoggledcoders.transport.block.rail.elevatorswitch;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ElevatorSwitchRailBlock extends AbstractRailBlock {
    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create("shape", RailShape.class, RailShape::isAscending);
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public ElevatorSwitchRailBlock(Properties properties) {
        super(true, properties);
    }

    public ElevatorSwitchRailBlock() {
        this(Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.7F)
                .sound(SoundType.METAL));
    }

    public static BlockState oppositeAscend(BlockState railState) {
        switch (railState.get(SHAPE)) {
            case ASCENDING_EAST:
                return railState.with(SHAPE, RailShape.ASCENDING_WEST);
            case ASCENDING_WEST:
                return railState.with(SHAPE, RailShape.ASCENDING_EAST);
            case ASCENDING_NORTH:
                return railState.with(SHAPE, RailShape.ASCENDING_SOUTH);
            case ASCENDING_SOUTH:
                return railState.with(SHAPE, RailShape.ASCENDING_NORTH);
        }
        return railState;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, TOP);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = super.getDefaultState();
        Direction direction = context.getPlacementHorizontalFacing();
        switch (direction) {
            case NORTH:
                blockstate = blockstate.with(SHAPE, RailShape.ASCENDING_NORTH);
                break;
            case EAST:
                blockstate = blockstate.with(SHAPE, RailShape.ASCENDING_EAST);
                break;
            case SOUTH:
                blockstate = blockstate.with(SHAPE, RailShape.ASCENDING_SOUTH);
                break;
            case WEST:
                blockstate = blockstate.with(SHAPE, RailShape.ASCENDING_WEST);
                break;
        }
        return blockstate.with(TOP, false);
    }

    @Override
    @Nonnull
    public IProperty<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.updateState(worldIn, pos, state);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            this.updateState(world, pos, state);
        }
    }

    private void updateState(World world, BlockPos pos, BlockState state) {
        if (!state.get(TOP)) {
            boolean powered = world.isBlockPowered(pos);
            if (powered && world.isAirBlock(pos.up())) {
                world.setBlockState(pos.up(), oppositeAscend(state).with(TOP, true),
                        Constants.BlockFlags.NOTIFY_NEIGHBORS);
                world.setBlockState(pos, TransportBlocks.ELEVATOR_SWITCH_SUPPORT.get().getDefaultState());
            }
        }
    }
}
