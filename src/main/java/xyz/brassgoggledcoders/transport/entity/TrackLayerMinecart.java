package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class TrackLayerMinecart extends AbstractMinecart {
    public TrackLayerMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public TrackLayerMinecart(EntityType<?> entityType, Level level, Vec3 vec3) {
        super(entityType, level, vec3.x(), vec3.y(), vec3.z());
    }

    @Override
    @Nonnull
    public Type getMinecartType() {
        return Type.CHEST;
    }
}
