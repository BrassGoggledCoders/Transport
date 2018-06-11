package xyz.brassgoggledcoders.transport.library.block.rail;

import net.minecraft.block.BlockRailBase.EnumRailDirection;

import java.util.function.Predicate;

public class RailPredicates {
    public static final Predicate<EnumRailDirection> FLAT_STRAIGHT = enumRailDirection ->
            enumRailDirection == EnumRailDirection.EAST_WEST || enumRailDirection == EnumRailDirection.NORTH_SOUTH;

    public static final Predicate<EnumRailDirection> ALL_CURVES = enumRailDirection ->
            enumRailDirection == EnumRailDirection.NORTH_WEST || enumRailDirection == EnumRailDirection.SOUTH_WEST ||
                    enumRailDirection == EnumRailDirection.NORTH_EAST || enumRailDirection == EnumRailDirection.SOUTH_EAST;
    public static final Predicate<EnumRailDirection> ALL_FLAT = enumRailDirection -> ALL_CURVES.or(FLAT_STRAIGHT).test(enumRailDirection);
}
