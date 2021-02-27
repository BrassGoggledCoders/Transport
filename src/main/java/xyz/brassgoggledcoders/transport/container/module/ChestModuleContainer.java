package xyz.brassgoggledcoders.transport.container.module;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.api.module.container.IModularContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;

public class ChestModuleContainer extends ModuleContainer {
    private final IItemHandler itemHandler;
    private final int rows;

    public ChestModuleContainer(int rows, IItemHandler itemHandler, IModularContainer modularContainer) {
        super(modularContainer);
        this.itemHandler = itemHandler;
        this.rows = rows;
    }

    @Override
    public void setup() {
        super.setup();
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new SlotItemHandler(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        ContainerHelper.addPlayerSlots(
                this.getModularContainer().getPlayerInventory(),
                this.getModularContainer()::putSlot,
                8,
                103 + (this.rows - 4) * 18
        );
    }

    public int getRows() {
        return rows;
    }
}
