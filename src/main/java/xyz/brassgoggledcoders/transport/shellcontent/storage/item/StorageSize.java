package xyz.brassgoggledcoders.transport.shellcontent.storage.item;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;

import java.util.Optional;

public enum StorageSize {
    THREE_BY_NINE(3, 9, ChestMenu::threeRows);

    private final int rows;
    private final int columns;
    private final Function3<Integer, Inventory, Container, AbstractContainerMenu> containerConstructor;

    StorageSize(int rows, int columns, Function3<Integer, Inventory, Container, AbstractContainerMenu> containerConstructor) {
        this.rows = rows;
        this.columns = columns;
        this.containerConstructor = containerConstructor;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getTotal() {
        return this.getColumns() * this.getRows();
    }

    public AbstractContainerMenu createMenu(int pContainerId, Inventory playerInventory, Container shellInventory) {
        return containerConstructor.apply(pContainerId, playerInventory, shellInventory);
    }

    public static Optional<StorageSize> getByName(String name) {
        for (StorageSize size : values()) {
            if (size.name().equalsIgnoreCase(name)) {
                return Optional.of(size);
            }
        }
        return Optional.empty();
    }

}
