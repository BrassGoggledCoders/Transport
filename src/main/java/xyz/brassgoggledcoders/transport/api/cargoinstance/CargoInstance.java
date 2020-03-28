package xyz.brassgoggledcoders.transport.api.cargoinstance;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoInstance implements ICapabilitySerializable<CompoundNBT> {
    private final Cargo cargo;

    public CargoInstance(Cargo cargo) {
        this.cargo = cargo;
    }

    @Nonnull
    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {

    }

    public void onTick() {

    }

    public int getComparatorLevel() {
        return -1;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        return LazyOptional.empty();
    }

    public BlockState getBlockState() {
        return cargo.getDefaultBlockState();
    }

    public ITextComponent getDisplayName() {
        return cargo.getDisplayName();
    }

    public ActionResultType applyInteraction(ICargoCarrier carrier, PlayerEntity player, Vec3d vec, Hand hand) {
        return ActionResultType.PASS;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
