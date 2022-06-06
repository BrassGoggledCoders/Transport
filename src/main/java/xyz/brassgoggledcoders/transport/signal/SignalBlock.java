package xyz.brassgoggledcoders.transport.signal;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

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
}
