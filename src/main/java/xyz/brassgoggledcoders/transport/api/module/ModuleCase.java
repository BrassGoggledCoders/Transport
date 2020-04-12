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

    public ModuleCase(IModularEntity modularEntity) {
        this.modularEntity = modularEntity;
        this.byModuleSlot = Maps.newHashMap();
        this.byModuleType = Maps.newHashMap();
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, ModuleSlot moduleSlot) {
        return this.addModule(module, moduleSlot, true);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, ModuleSlot moduleSlot, boolean sendUpdate) {
        if (modularEntity.canEquipModule(module) && module.isValidFor(modularEntity) && !byModuleSlot.containsKey(moduleSlot)) {
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

    @SuppressWarnings("unchecked")
    public <U extends ModuleInstance<T>, T extends Module<T>> U getByModuleType(ModuleType<T> moduleType) {
        return (U) byModuleType.get(moduleType);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT caseNBT = new CompoundNBT();
        ListNBT moduleNBT = new ListNBT();
        for (ModuleInstance<?> moduleInstance : byModuleSlot.values()) {
            CompoundNBT moduleInstanceNBT = new CompoundNBT();
            Module.toCompoundNBT(moduleInstance.getModule(), moduleInstanceNBT);
            CompoundNBT instanceNBT = moduleInstance.serializeNBT();
            instanceNBT.putUniqueId("uniqueId", moduleInstance.getUniqueId());
            moduleInstanceNBT.put("instance", instanceNBT);
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
                instanceNBT.remove("uniqueId");
                if (moduleInstance != null) {
                    moduleInstance.deserializeNBT(instanceNBT);
                }
            }
        }
    }

    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.getModules().size());
        for (ModuleInstance<?> moduleInstance : this.getModules()) {
            Module.toPacketBuffer(moduleInstance.getModule(), packetBuffer);
            packetBuffer.writeUniqueId(moduleInstance.getUniqueId());
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
        return Lists.newArrayList();
    }
}
