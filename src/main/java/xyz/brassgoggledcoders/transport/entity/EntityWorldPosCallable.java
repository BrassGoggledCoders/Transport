package xyz.brassgoggledcoders.transport.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiFunction;

public class EntityWorldPosCallable implements IWorldPosCallable {
    private final Entity entity;

    public EntityWorldPosCallable(Entity entity) {
        this.entity = entity;
    }

    @Override
    @Nonnull
    public <T> Optional<T> apply(@Nonnull BiFunction<World, BlockPos, T> blockPosTBiFunction) {
        if (entity.isAlive()) {
            return Optional.ofNullable(blockPosTBiFunction.apply(entity.world, entity.getPosition()));
        }
        return Optional.empty();
    }
}
