package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public abstract class NavigationPoint implements INavigationPoint {
    private final NavigationPointType pointType;
    private final BlockPos position;
    private UUID uniqueId;

    public NavigationPoint(NavigationPointType pointType, BlockPos position) {
        this.uniqueId = UUID.randomUUID();
        this.pointType = pointType;
        this.position = position;
    }

    @Nonnull
    @Override
    public BlockPos getPosition() {
        return position;
    }

    @Nonnull
    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void alertApproach(@Nonnull INavigator navigator, @Nonnull Entity entity) {

    }

    public abstract boolean addConnectedPoint(NavigationPoint navigationPoint);

    public abstract Collection<UUID> getConnectedPoints();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUniqueId("uniqueId", this.uniqueId);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.uniqueId = nbt.getUniqueId("uniqueId");
    }

    @Override
    public NavigationPointType getType() {
        return pointType;
    }
}
