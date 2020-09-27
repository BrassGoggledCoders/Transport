package xyz.brassgoggledcoders.transport.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldUtils {
    @SuppressWarnings("deprecation")
    public static boolean isBlockLoaded(@Nullable IBlockReader world, @Nonnull BlockPos pos) {
        if (world == null) {
            return false;
        } else if (world instanceof World) {
            return ((World) world).isBlockPresent(pos);
        } else if (world instanceof IWorldReader) {
            return ((IWorldReader) world).isBlockLoaded(pos);
        }
        return true;
    }
}
