package xyz.brassgoggledcoders.transport.signal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record SignalPoint(UUID uuid, Block block, BlockPos blockPos) {
    public static final SignalPoint EMPTY = new SignalPoint(
            UUID.nameUUIDFromBytes("empty".getBytes(StandardCharsets.UTF_8)),
            Blocks.AIR,
            BlockPos.ZERO
    );
}
