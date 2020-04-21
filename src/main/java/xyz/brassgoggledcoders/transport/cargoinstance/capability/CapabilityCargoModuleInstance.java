package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.container.ModuleContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class CapabilityCargoModuleInstance<CAP> extends CargoModuleInstance implements IScreenAddonProvider,
        IContainerAddonProvider {
    private final Capability<CAP> capability;

    public CapabilityCargoModuleInstance(CargoModule cargoModule, IModularEntity entity, Capability<CAP> capability) {
        super(cargoModule, entity);
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
    public ActionResultType applyInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        if (!player.isCrouching()) {
            this.getModularEntity().openContainer(player, new ModuleContainerProvider(this,
                    this.getModularEntity()), packetBuffer -> packetBuffer.writeResourceLocation(Objects.requireNonNull(
                    this.getModule().getType().getRegistryName())));
            return ActionResultType.SUCCESS;
        }
        return super.applyInteraction(player, vec, hand);
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
