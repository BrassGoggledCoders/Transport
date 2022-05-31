package xyz.brassgoggledcoders.transport.block.rail.signal;

import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.block.rail.TransportBlockStateProperties;

public class OneWaySignalRailBlock extends BaseRailBlock {
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final EnumProperty<SignalState> SIGNAL = TransportBlockStateProperties.SIGNAL_STATE;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public OneWaySignalRailBlock(Properties pProperties) {
        super(true, pProperties);
        this.registerDefaultState(this.getStateDefinition()
                .any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(SIGNAL, SignalState.STOP)
                .setValue(INVERTED, false)
                .setValue(BaseRailBlock.WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE, SIGNAL, INVERTED, BaseRailBlock.WATERLOGGED);
    }

    @Override
    @NotNull
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}
