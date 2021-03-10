package xyz.brassgoggledcoders.transport.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.swing.text.html.Option;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.BiFunction;

public class EntityWorldPosCallable implements IWorldPosCallable {
    private final WeakReference<Entity> weakEntity;
    private final WeakReference<World> weakWorld;

    public EntityWorldPosCallable(Entity entity) {
        this.weakEntity = new WeakReference<>(entity);
        this.weakWorld = new WeakReference<>(entity.getEntityWorld());
    }

    @Override
    @Nonnull
    public <T> Optional<T> apply(@Nonnull BiFunction<World, BlockPos, T> blockPosTBiFunction) {
        Entity entity = weakEntity.get();
        if (entity != null && entity.isAlive()) {
            return Optional.ofNullable(blockPosTBiFunction.apply(entity.getEntityWorld(), entity.getPosition()));
        } else {
            World world = weakWorld.get();
            if (world != null) {
                return Optional.ofNullable(blockPosTBiFunction.apply(world, BlockPos.ZERO));
            }
        }
        return Optional.empty();
    }
}
