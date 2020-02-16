package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CargoInstanceCap<CAP extends INBTSerializable<CompoundNBT>> implements ICargoInstance {
    private final Capability<CAP> capabilityType;
    private final CAP capabilityInstance;
    private final LazyOptional<CAP> lazyOptional;
    private final ITextComponent description;

    public CargoInstanceCap(ResourceLocation registryName, Capability<CAP> capabilityType, CAP capabilityInstance) {
        this.capabilityType = capabilityType;
        this.capabilityInstance = capabilityInstance;
        this.lazyOptional = LazyOptional.of(() -> capabilityInstance);
        this.description = new TranslationTextComponent(Util.makeTranslationKey("cargo", registryName));
    }

    @Override
    public ITextComponent getDescription() {
        return description;
    }

    @Nonnull
    @Override
    public CompoundNBT serializeNBT() {
        return capabilityInstance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        capabilityInstance.deserializeNBT(compoundNBT);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == capabilityType ? lazyOptional.cast() : LazyOptional.empty();
    }
}
