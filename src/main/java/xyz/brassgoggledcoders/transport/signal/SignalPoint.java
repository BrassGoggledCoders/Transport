package xyz.brassgoggledcoders.transport.signal;

import net.minecraft.core.BlockPos;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record SignalPoint(UUID uuid, BlockPos blockPos) {
    public static final SignalPoint EMPTY = new SignalPoint(
            UUID.nameUUIDFromBytes("empty".getBytes(StandardCharsets.UTF_8)),
            BlockPos.ZERO
    );
}
