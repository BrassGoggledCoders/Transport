package xyz.brassgoggledcoders.transport.api;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.RailShape;

public class TransportBlockStateProperties {
    public static final BooleanProperty NORTH_WEST = BooleanProperty.create("north_west");

    public static final EnumProperty<RailShape> STRAIGHT_RAIL_SHAPE = EnumProperty.create("shape", RailShape.class,
            railShape -> railShape == RailShape.NORTH_SOUTH || railShape == RailShape.EAST_WEST);

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
}
