package xyz.brassgoggledcoders.transport.cargoinstance.capabilitycargoinstance;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.container.cargo.CargoContainerProvider;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddonProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CapabilityCargoInstance<CAP> extends CargoInstance implements IScreenAddonProvider, IContainerAddonProvider {
    private final Capability<CAP> capability;

    public CapabilityCargoInstance(Cargo cargo, Capability<CAP> capability) {
        super(cargo);
        this.capability = capability;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == capability) {
            return this.getLazyOptional().cast();
        } else {
            return super.getCapability(cap, direction);
        }
    }

    @Override
    public ActionResultType applyInteraction(ICargoCarrier carrier, PlayerEntity player, Vec3d vec, Hand hand) {
        if (!player.isCrouching()) {
            carrier.openContainer(player, new CargoContainerProvider(this), packetBuffer -> {});
            return ActionResultType.SUCCESS;
        }
        return super.applyInteraction(carrier, player, vec, hand);
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        super.deserializeNBT(compoundNBT);
        this.deserializeCapability(compoundNBT.getCompound("capability"));
    }

    @Nonnull
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("capability", this.serializeCapability());
        return nbt;
    }

    protected abstract LazyOptional<CAP> getLazyOptional();

    protected abstract CompoundNBT serializeCapability();

    protected abstract void deserializeCapability(CompoundNBT nbt);
}
