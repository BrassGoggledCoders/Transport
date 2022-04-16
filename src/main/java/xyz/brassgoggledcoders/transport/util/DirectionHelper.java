package xyz.brassgoggledcoders.transport.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.function.Predicate;

public class DirectionHelper {
    private static final Map<Direction, Vec3> DIRECTION_POS = ImmutableMap.copyOf(
            Util.make(
                    Maps.newEnumMap(Direction.class),
                    map -> {
                        map.put(Direction.UP, new Vec3(0.5, 1, 0.5));
                        map.put(Direction.DOWN, new Vec3(0.5, 0, 0.5));
                        map.put(Direction.NORTH, new Vec3(0.5, 0.5, 0));
                        map.put(Direction.SOUTH, new Vec3(0.5, 0.5, 1));
                        map.put(Direction.WEST, new Vec3(0, 0.5, 0.5));
                        map.put(Direction.EAST, new Vec3(1, 0.5, 0.5));
                    }
            )
    );

    public static Direction getClosestVerticalSide(Vec3 hitVec) {
        return getClosestSide(hitVec, direction -> direction.getAxis() != Direction.Axis.Y);
    }

    public static Direction getClosestSide(Vec3 hitVec, Predicate<Direction> valid) {
        Vec3 blockSpace = new Vec3(
                toBlockSpace(hitVec.x()),
                toBlockSpace(hitVec.y()),
                toBlockSpace(hitVec.z())
        );
        Direction closestDirection = null;
        double closestDistance = 1.0;
        for (Map.Entry<Direction, Vec3> entry : DIRECTION_POS.entrySet()) {
            if (valid.test(entry.getKey())) {
                double distance = entry.getValue().distanceTo(blockSpace);
                if (distance < closestDistance) {
                    closestDirection = entry.getKey();
                    closestDistance = distance;
                }
            }
        }
        return closestDirection;
    }

    private static double toBlockSpace(double value) {
        double decimalValue = value - Math.floor(value);
        return decimalValue > 0 ? decimalValue : decimalValue + 1;
    }
}
