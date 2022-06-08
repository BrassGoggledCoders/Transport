package xyz.brassgoggledcoders.transport.signal;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.functional.Trial;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record SignalPoint(UUID uuid, Block block, BlockPos blockPos) {
    public static final SignalPoint EMPTY = new SignalPoint(
            UUID.nameUUIDFromBytes("empty".getBytes(StandardCharsets.UTF_8)),
            Blocks.AIR,
            BlockPos.ZERO
    );

    public CompoundTag toTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID("UUID", uuid);
        compoundTag.putString("Block", Optional.ofNullable(ForgeRegistries.BLOCKS.getKey(block))
                .map(ResourceLocation::toString)
                .orElse("minecraft:air")
        );
        compoundTag.put("BlockPos", NbtUtils.writeBlockPos(blockPos));
        return compoundTag;
    }

    public static Trial<SignalPoint> fromTag(CompoundTag compoundTag) {
        String name = compoundTag.getString("Block");
        return Optional.ofNullable(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)))
                .<Trial<SignalPoint>>map(block -> Trial.success(new SignalPoint(
                        compoundTag.getUUID("UUID"),
                        block,
                        NbtUtils.readBlockPos(compoundTag.getCompound("BlockPos"))
                )))
                .orElseGet(() -> Trial.warn("Failed to find Block with Name: " + name));
    }
}
