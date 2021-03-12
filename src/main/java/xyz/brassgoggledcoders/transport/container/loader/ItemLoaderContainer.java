package xyz.brassgoggledcoders.transport.container.loader;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.container.BasicContainer;

import javax.annotation.Nullable;

public class ItemLoaderContainer extends BasicContainer {
    public ItemLoaderContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                               IWorldPosCallable worldPosCallable, IItemHandler itemHandler) {
        super(type, id, worldPosCallable);

        for (int i = 0; i < 5; i++) {
            this.addSlot(new SlotItemHandler(itemHandler, i, 44 + (i * 18), 35));
        }
        ContainerHelper.addPlayerSlots(playerInventory, this::addSlot);
    }

    public ItemLoaderContainer(ContainerType<?> type, int id, PlayerInventory playerInventory) {
        this(type, id, playerInventory, IWorldPosCallable.DUMMY, new ItemStackHandler(5));
    }
}
