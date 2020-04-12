package xyz.brassgoggledcoders.transport.container.module;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlots;

import java.util.List;
import java.util.function.Supplier;

public class ModuleItemSlot extends SlotItemHandler {
    private final Supplier<IModularEntity> modularEntitySupplier;

    public ModuleItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Supplier<IModularEntity> modularEntitySupplier) {
        super(itemHandler, index, xPosition, yPosition);
        this.modularEntitySupplier = modularEntitySupplier;
    }

    public ModuleSlot getModuleRule() {
        IModularEntity modularEntity = this.modularEntitySupplier.get();
        if (modularEntity != null) {
            List<ModuleSlot> moduleSlots = modularEntity.getModuleCase().getModuleSlots();
            if (this.getSlotIndex() < moduleSlots.size()) {
                return moduleSlots.get(this.getSlotIndex());
            }
        }

        return ModuleSlots.NONE;
    }
}
