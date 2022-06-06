package xyz.brassgoggledcoders.transport.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public class MinecartHelper {
    public static void boostMinecart(BlockState state, BlockPos pos, Property<RailShape> railShapeProperty, AbstractMinecart minecart) {
        boolean inverted = state.getValue(BlockStateProperties.INVERTED);
        RailShape railShape = state.getValue(railShapeProperty);
        Vec3 normalized = minecart.getDeltaMovement().normalize();

        Direction direction = Direction.fromNormal(new BlockPos(normalized));

        if (direction != null) {
            Vec3 cartPos = minecart.getPosition(1);
            Vec3 blockPos = railShape.isAscending() ? Vec3.atCenterOf(pos) : Vec3.atBottomCenterOf(pos);
            if (cartPos.distanceTo(blockPos) < 0.25) {
                boolean shouldReverse = switch (railShape) {
                    case ASCENDING_NORTH, NORTH_SOUTH ->
                            (direction == Direction.NORTH && inverted) || (direction == Direction.SOUTH && !inverted);
                    case ASCENDING_SOUTH -> direction == Direction.SOUTH && inverted;
                    case ASCENDING_EAST -> direction == Direction.EAST && inverted;
                    case ASCENDING_WEST -> direction == Direction.WEST && inverted;
                    case EAST_WEST ->
                            (direction == Direction.EAST && !inverted) || (direction == Direction.WEST && inverted);
                    default -> false;
                };

                if (shouldReverse) {
                    Vec3 delta = minecart.getDeltaMovement();
                    minecart.setDeltaMovement(new Vec3(
                            delta.x() * -1D,
                            railShape.isAscending() ? delta.y() * -1D : delta.y(),
                            delta.z() * -1D
                    ));
                }
            }
        } else {
            Vec3i boostDirection = switch (railShape) {
                case NORTH_SOUTH, ASCENDING_NORTH ->
                        inverted ? Direction.SOUTH.getNormal() : Direction.NORTH.getNormal();
                case ASCENDING_SOUTH -> inverted ? Direction.NORTH.getNormal() : Direction.SOUTH.getNormal();
                case ASCENDING_EAST -> inverted ? Direction.WEST.getNormal() : Direction.EAST.getNormal();
                case EAST_WEST, ASCENDING_WEST -> inverted ? Direction.EAST.getNormal() : Direction.WEST.getNormal();
                default -> Vec3i.ZERO;
            };
            minecart.setDeltaMovement(Vec3.atLowerCornerOf(boostDirection));
        }
    }
}
