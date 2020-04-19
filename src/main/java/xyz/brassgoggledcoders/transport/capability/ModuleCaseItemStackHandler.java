package xyz.brassgoggledcoders.transport.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModuleCaseItemStackHandler implements IItemHandlerModifiable {
    private final Supplier<IModularEntity> modularEntity;
    private final Consumer<Void> onUpdate;

    public ModuleCaseItemStackHandler(Supplier<IModularEntity> modularEntity, Consumer<Void> onUpdate) {
        this.modularEntity = modularEntity;
        this.onUpdate = onUpdate;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        IModularEntity entity = modularEntity.get();
        if (!stack.isEmpty() && entity != null) {
            Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
            ModuleSlot moduleSlot = getModuleSlot(entity, slot);
            if (moduleSlot != null && module != null) {
                entity.getModuleCase().removeByModuleSlot(moduleSlot, false);
                entity.getModuleCase().addModule(module, moduleSlot, false);
                this.onUpdate.accept(null);
            }
        }
    }

    private ModuleSlot getModuleSlot(IModularEntity entity, int slot) {
        List<ModuleSlot> moduleSlots = entity.getModuleCase().getModuleSlots();
        if (slot >= 0 && slot < moduleSlots.size()) {
            return moduleSlots.get(slot);
        }
        return null;
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IModularEntity entity = modularEntity.get();
        if (entity != null) {
            ModuleSlot moduleSlot = this.getModuleSlot(entity, slot);
            if (moduleSlot != null) {
                ModuleInstance<?> moduleInstance = entity.getModuleCase().getByModuleSlot(moduleSlot);
                if (moduleInstance != null) {
                    return moduleInstance.asItemStack();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        IModularEntity entity = modularEntity.get();
        if (entity != null && isItemValid(slot, stack)) {
            ModuleSlot moduleSlot = this.getModuleSlot(entity, slot);
            if (entity.getModuleCase().getByModuleSlot(moduleSlot) == null) {
                ItemStack newStack = stack.copy();
                newStack.shrink(1);
                if (!simulate) {
                    Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
                    entity.getModuleCase().addModule(module, moduleSlot, false);
                    this.onUpdate.accept(null);
                }
                return newStack;
            }
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IModularEntity entity = modularEntity.get();
        if (entity != null) {
            ModuleSlot moduleSlot = this.getModuleSlot(entity, slot);
            if (moduleSlot != null) {
                ModuleInstance<?> moduleInstance = entity.getModuleCase().getByModuleSlot(moduleSlot);
                if (moduleInstance != null) {
                    ItemStack itemStack = moduleInstance.asItemStack();
                    if (!simulate) {
                        entity.getModuleCase().removeByModuleSlot(moduleSlot, false);
                        this.onUpdate.accept(null);
                    }
                    return itemStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        IModularEntity entity = modularEntity.get();
        if (!stack.isEmpty() && entity != null) {
            Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
            ModuleSlot moduleSlot = this.getModuleSlot(entity, slot);
            if (moduleSlot != null && module != null) {
                return entity.canEquipModule(module) && module.isValidFor(entity) && moduleSlot.isModuleValid(entity, module);
            }
        }

        return false;
    }

}
