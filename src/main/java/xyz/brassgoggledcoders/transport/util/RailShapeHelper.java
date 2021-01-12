package xyz.brassgoggledcoders.transport.util;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.properties.RailShape;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class RailShapeHelper {
    public static List<RailShape> CURVED_RAILS = Lists.newArrayList(RailShape.NORTH_EAST, RailShape.SOUTH_EAST,
            RailShape.SOUTH_WEST, RailShape.NORTH_WEST);
    public static List<RailShape> STRAIGHT_RAILS = Lists.newArrayList(RailShape.NORTH_SOUTH, RailShape.EAST_WEST);
    public static List<RailShape> ASCENDING_RAILS = Lists.newArrayList(RailShape.ASCENDING_NORTH, RailShape.ASCENDING_EAST,
            RailShape.ASCENDING_SOUTH, RailShape.ASCENDING_WEST);

    @Nonnull
    public static BlockState nextBending(BlockState blockState, Property<RailShape> property, boolean major) {
        if (major) {
            RailShape currentRailShape = blockState.get(property);
            Collection<RailShape> allowedValues = property.getAllowedValues();
            List<RailShape> tries = Lists.newArrayList();

            if (CURVED_RAILS.contains(currentRailShape)) {
                tries.addAll(STRAIGHT_RAILS);
                tries.addAll(ASCENDING_RAILS);
            } else if (STRAIGHT_RAILS.contains(currentRailShape)) {
                tries.addAll(ASCENDING_RAILS);
                tries.addAll(CURVED_RAILS);
            } else if (ASCENDING_RAILS.contains(currentRailShape)) {
                tries.addAll(CURVED_RAILS);
                tries.addAll(STRAIGHT_RAILS);
            }

            tries.retainAll(allowedValues);

            if (tries.isEmpty()) {
                return blockState;
            } else {
                return blockState.with(property, tries.get(0));
            }
        } else {
            return blockState.func_235896_a_(property);
        }
    }
}
