package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;
import xyz.brassgoggledcoders.transport.api.registry.ITransportRegistryItem;

import java.util.Optional;

public interface ICargo<T extends ICargoInstance> extends ITransportRegistryItem {
    default Optional<Gui> getGui(T cargoInstance) {
        return Optional.empty();
    }

    default Optional<Container> getContainer(T cargoInstance) {
        return Optional.empty();
    }

    default boolean onRightClick(T cargoInstance, EntityPlayer entityPlayer, EnumHand hand) {
        return false;
    }

    default void onTick(T cargoInstance) {

    }

    T create(World world);
}
