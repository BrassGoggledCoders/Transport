package xyz.brassgoggledcoders.transport.api.module.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;

public class ModuleContainer {
    private final IModularContainer modularContainer;

    public ModuleContainer(IModularContainer modularContainer) {
        this.modularContainer = modularContainer;
    }

    public void setup() {

    }

    protected void addPlayerSlots() {
        ContainerHelper.addPlayerSlots(
                this.getModularContainer().getPlayerInventory(),
                this.getModularContainer()::putSlot
        );
    }

    protected void addSlot(Slot slot) {
        this.getModularContainer()
                .putSlot(slot);
    }

    public IModularContainer getModularContainer() {
        return modularContainer;
    }

    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        return ContainerHelper.transferStackInSlot(
                this.getModularContainer().asContainer(),
                player,
                index,
                this.getModularContainer()::attemptMergeItemStack
        );
    }
}
