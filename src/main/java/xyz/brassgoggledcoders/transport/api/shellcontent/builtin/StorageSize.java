package xyz.brassgoggledcoders.transport.api.shellcontent.builtin;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.Optional;

public enum StorageSize {
    ONE_BY_NINE(1, 9, (id, inventory, container) -> new ChestMenu(MenuType.GENERIC_9x1, id, inventory, container, 1)),
    TWO_BY_NINE(2, 9, (id, inventory, container) -> new ChestMenu(MenuType.GENERIC_9x2, id, inventory, container, 2)),
    THREE_BY_NINE(3, 9, ChestMenu::threeRows),
    FOUR_BY_NINE(4, 9, (id, inventory, container) -> new ChestMenu(MenuType.GENERIC_9x4, id, inventory, container, 4)),
    FIVE_BY_NINE(5, 9, (id, inventory, container) -> new ChestMenu(MenuType.GENERIC_9x5, id, inventory, container, 5)),
    SIX_BY_NINE(6, 9, ChestMenu::sixRows),
    THREE_BY_THREE(3, 3, DispenserMenu::new);

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
