package xyz.brassgoggledcoders.transport.api.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ModuleContainerType<T extends ModuleContainer> extends ForgeRegistryEntry<ModuleContainerType<?>> {
    private final IFactory<T> factory;

    public ModuleContainerType(IFactory<T> factory) {
        this.factory = factory;
    }

    public T create(int windowId, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return this.factory.create(windowId, playerInventory, packetBuffer);
    }

    public interface IFactory<T extends ModuleContainer> {
        T create(int windowId, PlayerInventory playerInventory, PacketBuffer packetBuffer);
    }
}
