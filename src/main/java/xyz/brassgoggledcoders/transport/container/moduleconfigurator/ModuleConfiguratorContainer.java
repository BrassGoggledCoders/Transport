package xyz.brassgoggledcoders.transport.container.moduleconfigurator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.slot.ModularItemSlot;
import xyz.brassgoggledcoders.transport.container.slot.ModuleSlotSlot;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nonnull;

public class ModuleConfiguratorContainer extends Container {
    private final IWorldPosCallable worldPosCallable;

    public ModuleConfiguratorContainer(ContainerType<ModuleConfiguratorContainer> containerType, int windowId, PlayerInventory playerInventory) {
        this(containerType, windowId, playerInventory, IWorldPosCallable.DUMMY, new ModularItemStackHandler(
                playerInventory.player::getEntityWorld, () -> {
        }));
    }

    public ModuleConfiguratorContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable,
                                       ModularItemStackHandler modularItemStackHandler) {
        this(TransportContainers.MODULE_CONFIGURATOR.get(), id, playerInventory, worldPosCallable, modularItemStackHandler);
    }

    public ModuleConfiguratorContainer(ContainerType<ModuleConfiguratorContainer> containerType, int windowId,
                                       PlayerInventory playerInventory, IWorldPosCallable worldPosCallable,
                                       ModularItemStackHandler modularItemStackHandler) {
        super(containerType, windowId);
        this.worldPosCallable = worldPosCallable;
        this.addSlot(new ModularItemSlot(modularItemStackHandler, 0, 26, 33));
        for (int i = 0; i < 3; i++) {
            this.addSlot(new ModuleSlotSlot(modularItemStackHandler, i, 61, 17 + (i * 18)));
        }
        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return isWithinUsableDistance(worldPosCallable, player, TransportBlocks.MODULE_CONFIGURATOR.get());
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        return ContainerHelper.transferStackInSlot(this, player, index, this::mergeItemStack);
    }
}
