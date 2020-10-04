package xyz.brassgoggledcoders.transport.capability.supervisor;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class TileEntityCapabilitySupervisor implements ICapabilityProvider {
    private final WeakReference<TileEntity> tileEntity;
    private final WeakReference<World> world;

    private final Table<Capability<?>, NonnullDirection, LazyOptional<?>> supervisedLazies;

    public TileEntityCapabilitySupervisor(TileEntity tileEntity) {
        this.tileEntity = new WeakReference<>(tileEntity);
        this.world = new WeakReference<>(tileEntity.getWorld());
        this.supervisedLazies = HashBasedTable.create();
    }

    public boolean isValid() {
        return tileEntity.get() != null && world.get() != null &&
                Objects.requireNonNull(tileEntity.get()).getWorld() == world.get();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (this.isValid()) {
            NonnullDirection nonnullDirection = NonnullDirection.fromDirection(side);
            LazyOptional<?> lazyOptional = supervisedLazies.get(cap, nonnullDirection);
            if (lazyOptional == null) {
                lazyOptional = Objects.requireNonNull(tileEntity.get()).getCapability(cap, side);
                lazyOptional.addListener(listened -> supervisedLazies.remove(cap, nonnullDirection));
                supervisedLazies.put(cap, nonnullDirection, lazyOptional);
            }
            return lazyOptional.cast();
        } else {
            return LazyOptional.empty();
        }
    }
}
