package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class NavigationPointInstance implements INavigationPoint {
    private final World world;
    private final NavigationPointType pointType;
    private UUID uniqueId;

    public NavigationPointInstance(NavigationPointType pointType, World world) {
        this.uniqueId = UUID.randomUUID();
        this.world = world;
        this.pointType = pointType;
    }

    @Nonnull
    @Override
    public BlockPos getPosition() {
        return null;
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

    }
}
