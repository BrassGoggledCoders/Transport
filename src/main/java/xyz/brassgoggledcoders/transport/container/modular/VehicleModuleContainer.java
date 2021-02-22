package xyz.brassgoggledcoders.transport.container.modular;

import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.container.IModularContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;
import xyz.brassgoggledcoders.transport.container.slot.ModularItemSlot;
import xyz.brassgoggledcoders.transport.container.slot.ModuleSlotSlot;

public class VehicleModuleContainer extends ModuleContainer {
    private final ModularItemStackHandler modularItemStackHandler;

    public VehicleModuleContainer(IModularContainer modularContainer) {
        super(modularContainer);
        this.modularItemStackHandler = new ModularItemStackHandler(
                modularContainer.getPlayerInventory()
                        .player::getEntityWorld,
                () -> {
                }
        );
    }

    @Override
    public void setup() {
        super.setup();
        this.modularItemStackHandler.setStackInSlot(0, this.getModularContainer()
                .getModularEntity()
                .resolve()
                .map(IModularEntity::asItemStack)
                .orElse(ItemStack.EMPTY)
        );

        this.addSlot(new ModularItemSlot(modularItemStackHandler, 0, 26, 33, true));
        for (int i = 0; i < 3; i++) {
            this.addSlot(new ModuleSlotSlot(modularItemStackHandler, i, 61, 17 + (i * 18), true));
        }
        this.addPlayerSlots();
    }
}
