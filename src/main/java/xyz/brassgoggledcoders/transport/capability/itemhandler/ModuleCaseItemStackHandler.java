package xyz.brassgoggledcoders.transport.capability.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModuleCaseItemStackHandler implements IItemHandlerModifiable {
    private final Supplier<LazyOptional<IModularEntity>> modularEntity;
    private final Consumer<Void> onUpdate;

    public ModuleCaseItemStackHandler(Supplier<LazyOptional<IModularEntity>> modularEntity, Consumer<Void> onUpdate) {
        this.modularEntity = modularEntity;
        this.onUpdate = onUpdate;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        LazyOptional<IModularEntity> entity = modularEntity.get();
        entity.ifPresent(value -> {
            ModuleSlot moduleSlot = getModuleSlot(entity, slot);

            if (!stack.isEmpty()) {
                Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
                if (module != null) {
                    value.remove(moduleSlot, false);
                    value.add(module, moduleSlot, false);
                }
            } else {
                value.remove(moduleSlot, false);
            }
            this.onUpdate.accept(null);
        });
    }

    @Nonnull
    public ModuleSlot getModuleSlot(int slot) {
        return this.getModuleSlot(modularEntity.get(), slot);
    }

    @Nonnull
    private ModuleSlot getModuleSlot(LazyOptional<IModularEntity> entity, int slot) {
        return entity.map(IModularEntity::getModuleSlots)
                .filter(moduleSlots -> slot >= 0 && slot < moduleSlots.size())
                .map(moduleSlots -> moduleSlots.get(slot))
                .orElseGet(TransportModuleSlots.NONE::get);
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        ModuleSlot moduleSlot = this.getModuleSlot(modularEntity.get(), slot);

        return modularEntity.get()
                .map(value -> {
                    ModuleInstance<?> moduleInstance = value.getModuleInstance(moduleSlot);
                    if (moduleInstance != null) {
                        return moduleInstance.asItemStack();
                    } else {
                        return ItemStack.EMPTY;
                    }
                })
                .orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ModuleSlot moduleSlot = this.getModuleSlot(modularEntity.get(), slot);

        return modularEntity.get()
                .filter(value -> isItemValid(slot, stack))
                .filter(value -> value.getModuleInstance(moduleSlot) == null)
                .map(value -> {
                    ItemStack newStack = stack.copy();
                    newStack.shrink(1);
                    if (!simulate) {
                        Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
                        value.add(module, moduleSlot, false);
                        this.onUpdate.accept(null);
                    }
                    return newStack;
                })
                .orElse(stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ModuleSlot moduleSlot = this.getModuleSlot(modularEntity.get(), slot);
        return modularEntity.get()
                .map(value -> {
                    ModuleInstance<?> moduleInstance = value.getModuleInstance(moduleSlot);
                    if (moduleInstance != null) {
                        ItemStack itemStack = moduleInstance.asItemStack();
                        if (!simulate) {
                            value.remove(moduleSlot, false);
                            this.onUpdate.accept(null);
                        }
                        return itemStack;
                    }
                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
            ModuleSlot moduleSlot = this.getModuleSlot(modularEntity.get(), slot);
            if (module != null) {
                return modularEntity.get()
                        .map(value -> value.canEquip(module) && module.isValidFor(value) &&
                                moduleSlot.isModuleValid(value, module))
                        .orElse(false);
            }
        }

        return false;
    }

}
