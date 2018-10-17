package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.transport.api.cargo.carrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargo.render.ICargoRenderer;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface ICargoInstance extends ICapabilityProvider {
    @Nonnull
    default NBTTagCompound writeToNBT() {
        return new NBTTagCompound();
    }

    default void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    default void onTick() {

    }

    default Optional<Gui> getGui(ICargoCarrier cargoCarrier, EntityPlayer entityPlayer) {
        return Optional.empty();
    }

    default Optional<Container> getContainer(ICargoCarrier cargoCarrier, EntityPlayer entityPlayer) {
        return Optional.empty();
    }

    default boolean onRightClick(EntityPlayer entityPlayer, EnumHand hand) {
        return false;
    }

    @Nonnull
    ICargoRenderer getCargoRenderer();

    String getLocalizedName();
}
