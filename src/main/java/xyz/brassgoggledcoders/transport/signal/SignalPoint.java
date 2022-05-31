package xyz.brassgoggledcoders.transport.signal;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public record SignalPoint(UUID uuid, BlockPos blockPos) {
}
