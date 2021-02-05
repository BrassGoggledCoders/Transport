package xyz.brassgoggledcoders.transport.capability.itemhandler;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModularItemStackHandler implements IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
    private final Supplier<World> world;
    private final Runnable onChange;

    private boolean needsUpdate;
    private LazyOptional<IModularEntity> modularEntity;
    private ItemStack modularItemStack;

    public ModularItemStackHandler(Supplier<World> world, Runnable onChange) {
        this.world = world;
        this.onChange = onChange;
        this.modularEntity = LazyOptional.empty();
        this.modularItemStack = ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            this.modularItemStack = stack;
            this.onChange();
        } else {
            this.getModularEntity()
                    .ifPresent(value -> {
                        ModuleSlot moduleSlot = getModuleSlot(value, slot);
                        if (moduleSlot != null) {
                            if (!stack.isEmpty()) {
                                Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
                                if (module != null) {
                                    value.remove(moduleSlot, false);
                                    value.add(module, moduleSlot, false);
                                    this.writeChanges(value);
                                    this.onChange();
                                }
                            } else {
                                value.remove(moduleSlot, false);
                                this.writeChanges(value);
                                this.onChange();
                            }
                        }
                    });
        }
    }

    @Override
    public int getSlots() {
        return 1 + this.getModularEntity()
                .map(IModularEntity::getModuleSlots)
                .map(List::size)
                .orElse(0);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) {
            return this.modularItemStack;
        } else {
            return this.getModularEntity()
                    .map(entity -> {
                        ModuleSlot moduleSlot = this.getModuleSlot(entity, slot);
                        if (moduleSlot != null) {
                            ModuleInstance<?> moduleInstance = entity.getModuleInstance(moduleSlot);
                            if (moduleInstance != null) {
                                return moduleInstance.asItemStack();
                            }
                        }
                        return ItemStack.EMPTY;
                    })
                    .orElse(ItemStack.EMPTY);
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate) {
        if (slot == 0 && this.modularItemStack.isEmpty()) {
            ItemStack copiedStack = itemStack.copy();
            ItemStack inserted = copiedStack.split(1);
            if (!simulate) {
                this.modularItemStack = inserted;
                this.onChange();
            }
            return copiedStack;
        } else if (slot > 0) {
            return this.getModularEntity()
                    .filter(value -> isItemValid(slot, itemStack))
                    .map(value -> {
                        ModuleSlot moduleSlot = this.getModuleSlot(value, slot);
                        if (moduleSlot != null) {
                            ItemStack newStack = itemStack.copy();
                            newStack.shrink(1);
                            if (!simulate) {
                                Module<?> module = TransportAPI.getModuleFromItem(itemStack.getItem());
                                value.add(module, moduleSlot, false);
                                this.writeChanges(value);
                                this.onChange();
                            }
                            return newStack;
                        }
                        return itemStack;
                    })
                    .orElse(itemStack);
        }
        return itemStack;
    }

    private void writeChanges(IModularEntity modularEntity) {
        if (!this.modularItemStack.isEmpty()) {
            this.modularItemStack.getOrCreateTag().put("modules", modularEntity.serializeNBT());
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0) {
            ItemStack extracted = this.modularItemStack;
            if (!simulate) {
                this.modularItemStack = ItemStack.EMPTY;
                this.onChange();
            }
            return extracted;
        } else if (slot > 0) {
            return this.getModularEntity()
                    .map(value -> {
                        ModuleSlot moduleSlot = this.getModuleSlot(value, slot);
                        ModuleInstance<?> moduleInstance = value.getModuleInstance(moduleSlot);
                        if (moduleInstance != null) {
                            ItemStack itemStack = moduleInstance.asItemStack();
                            if (!simulate) {
                                value.remove(moduleSlot, false);
                                this.writeChanges(value);
                                this.onChange();
                            }
                            return itemStack;
                        }
                        return ItemStack.EMPTY;
                    })
                    .orElse(ItemStack.EMPTY);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            return stack.getItem() instanceof IModularItem<?>;
        } else if (slot > 0 && !stack.isEmpty()) {
            return this.getModularEntity()
                    .map(value -> {
                        ModuleSlot moduleSlot = this.getModuleSlot(value, slot);
                        Module<?> module = TransportAPI.getModuleFromItem(stack.getItem());
                        if (module != null && moduleSlot != null) {
                            return value.canEquip(module) && module.isValidFor(value) &&
                                    moduleSlot.isModuleValid(value, module);
                        } else {
                            return false;
                        }
                    })
                    .orElse(false);
        }
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return modularItemStack.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("Items")) {
            ListNBT items = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
            if (!items.isEmpty()) {
                this.modularItemStack = ItemStack.read(items.getCompound(0));
            }
        } else {
            this.modularItemStack = ItemStack.read(nbt);
        }
        this.needsUpdate = true;
    }

    public LazyOptional<IModularEntity> getModularEntity() {
        checkNeedsUpdate();
        return modularEntity;
    }

    private void checkNeedsUpdate() {
        if (needsUpdate) {
            if (this.modularItemStack.isEmpty()) {
                this.modularEntity = LazyOptional.empty();
            } else {
                if (this.modularItemStack.getItem() instanceof IModularItem<?>) {
                    Entity entity = ((IModularItem<?>) this.modularItemStack.getItem())
                            .getEntityType()
                            .create(Objects.requireNonNull(this.world.get()));
                    CompoundNBT instanceNBT = this.modularItemStack.getChildTag("modules");
                    if (entity != null) {
                        modularEntity = entity.getCapability(TransportAPI.MODULAR_ENTITY);
                        if (instanceNBT != null) {
                            modularEntity.ifPresent(value -> value.deserializeNBT(instanceNBT));
                        }
                    }
                }
            }
            this.needsUpdate = false;
        }
    }

    private void onChange() {
        this.needsUpdate = true;
        this.onChange.run();
    }

    @Nullable
    private ModuleSlot getModuleSlot(IModularEntity entity, int slot) {
        List<ModuleSlot> moduleSlots = entity.getModuleSlots();
        int actualSlot = slot - 1;
        if (actualSlot >= 0 && moduleSlots.size() > actualSlot) {
            return moduleSlots.get(actualSlot);
        } else {
            return null;
        }
    }

    public List<ModuleSlot> getAvailableModuleSlots() {
        return this.getModularEntity()
                .map(IModularEntity::getModuleSlots)
                .orElseGet(Lists::newArrayList);
    }

    public void forEach(Consumer<ItemStack> itemStackConsumer) {
        itemStackConsumer.accept(this.modularItemStack);
        this.modularItemStack = ItemStack.EMPTY;
        this.onChange();
    }
}
