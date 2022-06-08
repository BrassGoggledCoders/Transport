package xyz.brassgoggledcoders.transport.signal;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class SignalBlock {
    public static final SignalBlock EMPTY = new SignalBlock(
            UUID.nameUUIDFromBytes("empty".getBytes(StandardCharsets.UTF_8)),
            ImmutableSet.of()
    );

    private final UUID uuid;
    private final Set<UUID> occupyingMinecarts;

    public SignalBlock(UUID uuid) {
        this(uuid, Sets.newHashSet());
    }

    public SignalBlock(UUID uuid, Set<UUID> occupyingMinecarts) {
        this.uuid = uuid;
        this.occupyingMinecarts = occupyingMinecarts;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<UUID> getOccupyingMinecarts() {
        return occupyingMinecarts;
    }

    public boolean isOccupied() {
        return !this.getOccupyingMinecarts()
                .isEmpty();
    }

    public CompoundTag toTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID("UUID", this.getUuid());
        ListTag occupyingMinecartTag = new ListTag();
        for (UUID occupyingUUID : this.getOccupyingMinecarts()) {
            occupyingMinecartTag.add(NbtUtils.createUUID(occupyingUUID));
        }
        compoundTag.put("OccupyingMinecarts", occupyingMinecartTag);
        return compoundTag;
    }

    public static SignalBlock fromTag(CompoundTag compoundTag) {
        ListTag occupyMinecartsTag = compoundTag.getList("OccupyingMinecarts", Tag.TAG_INT_ARRAY);
        Set<UUID> occupyingMinecarts = Sets.newHashSet();
        for (Tag tag : occupyMinecartsTag) {
            occupyingMinecarts.add(NbtUtils.loadUUID(tag));
        }

        return new SignalBlock(
                compoundTag.getUUID("UUID"),
                occupyingMinecarts
        );
    }
}
