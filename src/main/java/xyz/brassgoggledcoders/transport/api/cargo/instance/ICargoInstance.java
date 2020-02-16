package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICargoInstance extends ICapabilitySerializable<CompoundNBT> {
    @Nonnull
    @Override
    default CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    default void deserializeNBT(CompoundNBT compoundNBT) {

    }

    default void onTick() {

    }

    default boolean onInteraction(PlayerEntity entityPlayer, Hand hand) {
        return false;
    }

    ITextComponent getDescription();

    @Nonnull
    @Override
    default <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        return LazyOptional.empty();
    }
}
