package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import xyz.brassgoggledcoders.transport.block.rail.signal.SignalState;

public class TransportBlockStateProperties {
    public static EnumProperty<RailShape> FLAT_STRAIGHT_RAIL_SHAPE = EnumProperty.create(
            "shape",
            RailShape.class,
            RailShape.EAST_WEST, RailShape.NORTH_SOUTH
    );

    public static EnumProperty<SignalState> SIGNAL_STATE = EnumProperty.create(
            "signal",
            SignalState.class
    );
}
