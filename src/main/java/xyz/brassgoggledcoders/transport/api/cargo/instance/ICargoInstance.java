package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

import javax.annotation.Nonnull;

public interface ICargoInstance extends ICapabilityProvider {
    @Nonnull
    default NBTTagCompound writeToNBT() {
        return new NBTTagCompound();
    }

    default void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Nonnull
    ICargoRenderer getCargoRenderer();

    String getLocalizedName();
}
