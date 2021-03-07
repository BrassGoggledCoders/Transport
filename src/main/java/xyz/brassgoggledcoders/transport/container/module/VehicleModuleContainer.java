package xyz.brassgoggledcoders.transport.container.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;
import xyz.brassgoggledcoders.transport.container.slot.ModularItemSlot;
import xyz.brassgoggledcoders.transport.container.slot.ModuleSlotSlot;
import xyz.brassgoggledcoders.transport.util.WorldHelper;

import javax.annotation.Nonnull;

public class VehicleModuleContainer extends Container {
    private final IWorldPosCallable worldPosCallable;

    public <T extends Container> VehicleModuleContainer(
            ContainerType<T> type,
            int id,
            PlayerInventory playerInventory,
            ModularItemStackHandler handler,
            IWorldPosCallable worldPosCallable
    ) {
        super(type, id);
        this.worldPosCallable = worldPosCallable;

        this.addSlot(new ModularItemSlot(handler, 0, 26, 33, true));
        for (int i = 0; i < 3; i++) {
            this.addSlot(new ModuleSlotSlot(handler, i, 61, 17 + (i * 18), true));
        }
        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    public <T extends Container> VehicleModuleContainer(ContainerType<T> type, int id, PlayerInventory playerInventory) {
        this(type, id, playerInventory, new ModularItemStackHandler(playerInventory.player::getEntityWorld, () -> {
        }), IWorldPosCallable.DUMMY);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return worldPosCallable.applyOrElse(WorldHelper.isPlayerNear(player)::test, true);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        return ContainerHelper.transferStackInSlot(this, player, index, this::mergeItemStack);
    }
}
