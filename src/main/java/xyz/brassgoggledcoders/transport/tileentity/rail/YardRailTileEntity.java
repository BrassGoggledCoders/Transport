package xyz.brassgoggledcoders.transport.tileentity.rail;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IHoldable;
import xyz.brassgoggledcoders.transport.api.manager.IManageable;
import xyz.brassgoggledcoders.transport.api.manager.Manageable;
import xyz.brassgoggledcoders.transport.api.manager.ManagerType;
import xyz.brassgoggledcoders.transport.util.TickTimer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.UUID;

public class YardRailTileEntity extends TileEntity implements ITickableTileEntity {
    private final IManageable manageable;
    private final LazyOptional<IManageable> manageableLazy;

    private final Map<UUID, Pair<TickTimer, YardState>> entityYardState;

    public YardRailTileEntity(TileEntityType<? extends YardRailTileEntity> tileEntityType) {
        super(tileEntityType);
        this.manageable = new Manageable(ManagerType.RAIL);
        this.manageableLazy = LazyOptional.of(() -> manageable);
        this.entityYardState = Maps.newHashMap();
    }

    public void onMinecartPass(AbstractMinecartEntity minecartEntity) {
        if (!minecartEntity.getEntityWorld().isRemote()) {
            Pair<TickTimer, YardState> yardState = entityYardState.get(minecartEntity.getUniqueID());
            if (yardState != null) {
                yardState.getLeft().resetTimeToLive();
                switch (yardState.getRight()) {
                    case IGNORED:
                        break;
                    case ARRIVING:
                        handleArriving(minecartEntity);
                        break;
                    case UNLOADING:
                        handleUnloading(minecartEntity);
                        break;
                    case LOADING:
                        handleLoading(minecartEntity);
                        break;
                    case DEPARTING:
                        handleDeparting(minecartEntity);
                        break;
                }
            } else {
                if (TransportAPI.getConnectionChecker().getLeader(minecartEntity) == null) {
                    this.updateYardState(minecartEntity, YardState.ARRIVING);
                } else {
                    this.updateYardState(minecartEntity, YardState.IGNORED);
                }
            }
        }

    }

    private void handleArriving(AbstractMinecartEntity minecartEntity) {
        if (minecartEntity.getPosition().equals(this.getPos())) {
            minecartEntity.setMotion(Vector3d.ZERO);
            if (minecartEntity instanceof IHoldable) {
                ((IHoldable) minecartEntity).onHeld();
            }
            this.updateYardState(minecartEntity, YardState.UNLOADING);
        }
    }

    private void handleUnloading(AbstractMinecartEntity minecartEntity) {
        if (minecartEntity.getEntityWorld().getGameTime() % 100 == 0) {
            this.updateYardState(minecartEntity, YardState.LOADING);
        }
    }

    private void handleLoading(AbstractMinecartEntity minecartEntity) {
        if (minecartEntity.getEntityWorld().getGameTime() % 100 == 0) {
            this.updateYardState(minecartEntity, YardState.DEPARTING);
        }
    }

    private void handleDeparting(AbstractMinecartEntity minecartEntity) {
        if (minecartEntity instanceof IHoldable) {
            ((IHoldable) minecartEntity).onRelease();
        }
        this.updateYardState(minecartEntity, YardState.IGNORED);
    }

    private void updateYardState(AbstractMinecartEntity minecartEntity, YardState yardState) {
        Transport.LOGGER.info("Setting Minecart to " + yardState.name());
        Pair<TickTimer, YardState> currentYardState = entityYardState.get(minecartEntity.getUniqueID());
        if (currentYardState == null) {
            entityYardState.put(minecartEntity.getUniqueID(), Pair.of(new TickTimer(5), yardState));
        } else {
            entityYardState.put(minecartEntity.getUniqueID(), Pair.of(currentYardState.getLeft(), yardState));
        }
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == TransportAPI.MANAGEABLE) {
            return manageableLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {

    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt = super.write(nbt);
        nbt.put("manageable", this.manageable.serializeNBT());
        return nbt;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.manageable.deserializeNBT(nbt.getCompound("manageable"));
    }

    @Override
    public void tick() {
        entityYardState.values().removeIf(pair -> !pair.getLeft().tick(true));
    }
}
