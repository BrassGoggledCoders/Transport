package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import xyz.brassgoggledcoders.transport.block.rail.portal.PortalState;

public class TransportBlockStateProperties {
    public static EnumProperty<RailShape> FLAT_STRAIGHT_RAIL_SHAPE = EnumProperty.create(
            "shape",
            RailShape.class,
            RailShape.EAST_WEST, RailShape.NORTH_SOUTH
    );

    public static EnumProperty<PortalState> PORTAL_STATE = EnumProperty.create(
            "portal",
            PortalState.class
    );
}
