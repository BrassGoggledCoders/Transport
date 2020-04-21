package xyz.brassgoggledcoders.transport.api.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlots;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ModuleCase implements INBTSerializable<CompoundNBT> {
    private final IModularEntity modularEntity;
    private final Map<ModuleSlot, ModuleInstance<?>> byModuleSlot;
    private final Map<ModuleType<?>, ModuleInstance<?>> byModuleType;
    private final List<ModuleSlot> moduleSlots;

    public ModuleCase(IModularEntity modularEntity, ModuleSlot... moduleSlots) {
        this.modularEntity = modularEntity;
        this.byModuleSlot = Maps.newHashMap();
        this.byModuleType = Maps.newHashMap();
        this.moduleSlots = Lists.newArrayList(moduleSlots);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, ModuleSlot moduleSlot) {
        return this.addModule(module, moduleSlot, true);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, ModuleSlot moduleSlot, boolean sendUpdate) {
        if (modularEntity.canEquipModule(module) && module.isValidFor(modularEntity) && !byModuleSlot.containsKey(moduleSlot)
                && this.getModuleSlots().contains(moduleSlot)) {
            ModuleInstance<T> moduleInstance = module.createInstance(modularEntity);
            byModuleSlot.put(moduleSlot, moduleInstance);
            byModuleType.put(moduleInstance.getModuleType(), moduleInstance);
            if (sendUpdate) {
                TransportAPI.getNetworkHandler().sendAddModuleCase(this.modularEntity, moduleInstance, moduleSlot);
            }
            return moduleInstance;
        } else {
            return null;
        }
    }

    public ItemStack createItemStack() {
        return new ItemStack(modularEntity.asItem());
    }

    public Collection<ModuleInstance<?>> getModules() {
        return byModuleSlot.values();
    }

    @Nullable
    public ModuleInstance<?> getByModuleSlot(ModuleSlot moduleSlot) {
        return byModuleSlot.get(moduleSlot);
    }

    public void removeByModuleSlot(ModuleSlot moduleSlot) {
        ModuleInstance<?> moduleInstance = byModuleSlot.remove(moduleSlot);
        if (moduleInstance != null) {
            byModuleType.remove(moduleInstance.getModuleType());
        }
    }

    @SuppressWarnings("unchecked")
    public <U extends ModuleInstance<T>, T extends Module<T>> U getByModuleType(ModuleType<T> moduleType) {
        return (U) byModuleType.get(moduleType);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT caseNBT = new CompoundNBT();
        ListNBT moduleNBT = new ListNBT();
        for (Map.Entry<ModuleSlot, ModuleInstance<?>> entrySet: byModuleSlot.entrySet()) {
            CompoundNBT moduleInstanceNBT = new CompoundNBT();
            ModuleInstance<?> moduleInstance = entrySet.getValue();
            Module.toCompoundNBT(moduleInstance.getModule(), moduleInstanceNBT);
            CompoundNBT instanceNBT = moduleInstance.serializeNBT();
            moduleInstanceNBT.put("instance", instanceNBT);
            moduleInstanceNBT.putString("moduleSlot", entrySet.getKey().getName());
            moduleNBT.add(moduleInstanceNBT);
        }
        caseNBT.put("moduleInstances", moduleNBT);
        return caseNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT moduleInstancesNBT = nbt.getList("moduleInstances", Constants.NBT.TAG_COMPOUND);
        for (int x = 0; x < moduleInstancesNBT.size(); x++) {
            CompoundNBT moduleInstanceNBT = moduleInstancesNBT.getCompound(x);
            Module<?> module = Module.fromCompoundNBT(moduleInstanceNBT);
            ModuleSlot moduleSlot = ModuleSlots.MODULE_SLOT_MAP.get(moduleInstanceNBT.getString("moduleSlot"));
            if (module != null && moduleSlot != null) {
                CompoundNBT instanceNBT = moduleInstanceNBT.getCompound("instance");
                ModuleInstance<?> moduleInstance = this.addModule(module, moduleSlot, false);
                if (moduleInstance != null) {
                    moduleInstance.deserializeNBT(instanceNBT);
                }
            }
        }
    }

    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(byModuleSlot.size());
        for (Map.Entry<ModuleSlot, ModuleInstance<?>> entrySet: byModuleSlot.entrySet()) {
            Module.toPacketBuffer(entrySet.getValue().getModule(), packetBuffer);
            packetBuffer.writeString(entrySet.getKey().getName(), 64);
        }
    }

    public void read(PacketBuffer packetBuffer) {
        byModuleSlot.clear();
        byModuleType.clear();
        int components = packetBuffer.readInt();
        for (int x = 0; x < components; x++) {
            Module<?> module = Module.fromPacketBuffer(packetBuffer);
            ModuleSlot moduleSlot = ModuleSlots.MODULE_SLOT_MAP.get(packetBuffer.readString(64));
            if (module != null && moduleSlot != null) {
                this.addModule(module, moduleSlot, false);
            }
        }
    }

    public void removeByModuleSlot(ModuleSlot moduleSlot, boolean sendUpdate) {
        ModuleInstance<?> moduleInstance = this.byModuleSlot.remove(moduleSlot);
        if (moduleInstance != null) {
            byModuleType.remove(moduleInstance.getModuleType());
            if (sendUpdate) {
                //TODO need send?
                //TransportAPI.getNetworkHandler().sendAddModuleCase(modularEntity, moduleInstance, false);
            }
        }
    }

    public List<ModuleSlot> getModuleSlots() {
        return this.moduleSlots;
    }
}
