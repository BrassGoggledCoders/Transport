package xyz.brassgoggledcoders.transport.api.module;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

public class ModuleCase implements INBTSerializable<CompoundNBT> {
    private final IModularEntity modularEntity;
    private final Table<ModuleType<?>, Module<?>, ModuleInstance<?>> equippedModules;

    public ModuleCase(IModularEntity modularEntity) {
        this.modularEntity = modularEntity;
        this.equippedModules = HashBasedTable.create();
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module) {
        if (modularEntity.canEquipComponent(module) && module.isValidFor(modularEntity)) {
            ModuleInstance<T> moduleInstance = module.createInstance(modularEntity);
            equippedModules.put(module.getType(), module, moduleInstance);
            return moduleInstance;
        } else {
            return null;
        }
    }

    public ItemStack createItemStack() {
        return new ItemStack(modularEntity.asItem());
    }

    public Collection<ModuleInstance<?>> getComponents() {
        return equippedModules.values();
    }

    @SuppressWarnings("unchecked")
    public <U extends ModuleInstance<T>, T extends Module<T>> Collection<? extends U> getComponentInstances(ModuleType<T> moduleType) {
        return (Collection<U>) equippedModules.row(moduleType)
                .values();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT caseNBT = new CompoundNBT();
        ListNBT moduleNBT = new ListNBT();
        for (Table.Cell<ModuleType<?>, Module<?>, ModuleInstance<?>> cell : equippedModules.cellSet()) {
            CompoundNBT cellNBT = new CompoundNBT();
            cellNBT.putString("type", String.valueOf(Objects.requireNonNull(cell.getRowKey()).getRegistryName()));
            cellNBT.putString("module", String.valueOf(Objects.requireNonNull(cell.getColumnKey()).getRegistryName()));
            cellNBT.put("instance", Objects.requireNonNull(cell.getValue()).serializeNBT());
            moduleNBT.add(cellNBT);
        }
        caseNBT.put("modules", moduleNBT);
        return caseNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT moduleNBT = nbt.getList("modules", Constants.NBT.TAG_COMPOUND);
        for (int x = 0; x < moduleNBT.size(); x++) {
            CompoundNBT cellNBT = moduleNBT.getCompound(x);
            ModuleType<?> moduleType = TransportAPI.getModuleType(cellNBT.getString("type"));
            if (moduleType != null) {
                Module<?> module = (Module<?>) moduleType.load(cellNBT.getString("module"));
                if (module != null) {
                    ModuleInstance<?> moduleInstance = module.createInstance(this.modularEntity);
                    moduleInstance.deserializeNBT(cellNBT.getCompound("instance"));
                    equippedModules.put(moduleType, module, moduleInstance);
                }
            }
        }
    }

    public void write(PacketBuffer buffer) {
        buffer.writeInt(this.getComponents().size());
        for (ModuleInstance<?> moduleInstance : this.getComponents()) {
            buffer.writeResourceLocation(Objects.requireNonNull(moduleInstance.getModule().getType().getRegistryName()));
            buffer.writeResourceLocation(Objects.requireNonNull(moduleInstance.getModule().getRegistryName()));
        }
    }

    public void read(PacketBuffer buffer) {
        equippedModules.clear();
        int components = buffer.readInt();
        for (int x = 0; x < components; x++) {
            ModuleType<?> moduleType = TransportAPI.getModuleType(buffer.readResourceLocation());
            ResourceLocation componentRegistryName = buffer.readResourceLocation();
            if (moduleType != null) {
                this.addModule((Module<?>) moduleType.load(componentRegistryName));
            }
        }
    }
}
