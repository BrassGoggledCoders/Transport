package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import xyz.brassgoggledcoders.transport.navigation.NavigationNetwork;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public abstract class NavigationPoint implements INavigationPoint {
    private final NavigationPointType pointType;
    private final INavigationNetwork navigationNetwork;
    private BlockPos position;
    private UUID uniqueId;

    public NavigationPoint(INavigationNetwork navigationNetwork, NavigationPointType pointType) {
        this.navigationNetwork = navigationNetwork;
        this.uniqueId = UUID.randomUUID();
        this.pointType = pointType;
        this.position = BlockPos.ZERO;
    }

    @Override
    public void setPosition(BlockPos blockPos) {
        this.position = blockPos;
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

    @Override
    public INavigationNetwork getNetwork() {
        return navigationNetwork;
    }
}
