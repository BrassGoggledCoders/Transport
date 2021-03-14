package xyz.brassgoggledcoders.transport.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WorldHelper {
    public static BiPredicate<World, BlockPos> isPlayerNear(PlayerEntity playerEntity) {
        WeakReference<PlayerEntity> playerEntityWeakReference = new WeakReference<>(playerEntity);
        return (world, blockPos) -> {
            PlayerEntity playerReference = playerEntityWeakReference.get();
            if (playerReference != null) {
                return playerEntity.getDistanceSq((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D,
                        (double) blockPos.getZ() + 0.5D) <= 64.0D;
            } else {
                return false;
            }
        };
    }


    public static boolean isEntityChunkLoaded(final IWorld world, final ChunkPos pos) {
        return world.getChunkProvider().isChunkLoaded(pos);
    }

    public static boolean isEntityChunkLoaded(final IWorld world, final int x, final int z) {
        return isEntityChunkLoaded(world, new ChunkPos(x, z));
    }

    public static <T extends Entity> List<T> getEntitiesWithinAABB(
            final @Nonnull IWorld world,
            final @Nonnull Class<? extends T> clazz,
            final @Nonnull AxisAlignedBB axisAlignedBB,
            @Nullable final Predicate<? super T> predicate
    ) {

        int minX = (int) (Math.round(axisAlignedBB.minX) >> 4);
        int maxX = (int) (Math.round(axisAlignedBB.maxX) >> 4);
        int minZ = (int) (Math.round(axisAlignedBB.minZ) >> 4);
        int maxZ = (int) (Math.round(axisAlignedBB.minZ) >> 4);
        int minY = (int) (Math.round(axisAlignedBB.minY) >> 4);
        int maxY = (int) (Math.round(axisAlignedBB.maxY) >> 4);

        List<T> list = Lists.newArrayList();
        AbstractChunkProvider abstractchunkprovider = world.getChunkProvider();

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (isEntityChunkLoaded(world, x, z)) {
                    Chunk chunk = abstractchunkprovider.getChunkNow(x, z);
                    if (chunk != null) {
                        for (int y = minY; y <= maxY; y++) {
                            for (final T entity : chunk.getEntityLists()[y].getByClass(clazz)) {
                                if (axisAlignedBB.contains(entity.getPositionVec()) && (predicate == null || predicate.test(entity))) {
                                    list.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        }

        return list;
    }
}
