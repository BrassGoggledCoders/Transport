package xyz.brassgoggledcoders.transport.api.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import xyz.brassgoggledcoders.transport.api.TransportAPI;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class ModuleCase implements INBTSerializable<CompoundNBT> {
    private final IModularEntity modularEntity;
    private final Map<UUID, ModuleInstance<?>> byUUID;
    private final Multimap<Module<?>, ModuleInstance<?>> byModule;
    private final Multimap<ModuleType<?>, ModuleInstance<?>> byModuleType;

    public ModuleCase(IModularEntity modularEntity) {
        this.modularEntity = modularEntity;
        this.byUUID = Maps.newHashMap();
        this.byModule = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
        this.byModuleType = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module) {
        return this.addModule(module, UUID.randomUUID(), true);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, boolean sendUpdate) {
        return this.addModule(module, UUID.randomUUID(), sendUpdate);
    }

    @Nullable
    public <T extends Module<T>> ModuleInstance<T> addModule(Module<T> module, UUID forcedUniqueId, boolean sendUpdate) {
        if (modularEntity.canEquipModule(module) && module.isValidFor(modularEntity)) {
            ModuleInstance<T> moduleInstance = module.createInstance(modularEntity);
            moduleInstance.setUniqueId(forcedUniqueId);
            byUUID.put(moduleInstance.getUniqueId(), moduleInstance);
            byModule.get(moduleInstance.getModule()).add(moduleInstance);
            byModuleType.get(moduleInstance.getModuleType()).add(moduleInstance);
            if (sendUpdate) {
                TransportAPI.getNetworkHandler().sendModuleCaseUpdate(this.modularEntity, moduleInstance, true);
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
        return byUUID.values();
    }

    @SuppressWarnings("unchecked")
    public <U extends ModuleInstance<T>, T extends Module<T>> Collection<? extends U> getComponentInstances(ModuleType<T> moduleType) {
        return (Collection<? extends U>) byModuleType.get(moduleType);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT caseNBT = new CompoundNBT();
        ListNBT moduleNBT = new ListNBT();
        for (ModuleInstance<?> moduleInstance : byUUID.values()) {
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
            if (module != null) {
                CompoundNBT instanceNBT = moduleInstanceNBT.getCompound("instance");
                ModuleInstance<?> moduleInstance = this.addModule(
                        module,
                        instanceNBT.getUniqueId("uniqueId"),
                        false);
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
        byUUID.clear();
        byModule.clear();
        byModuleType.clear();
        int components = packetBuffer.readInt();
        for (int x = 0; x < components; x++) {
            Module<?> module = Module.fromPacketBuffer(packetBuffer);
            UUID uniqueId = packetBuffer.readUniqueId();
            if (module != null) {
                this.addModule(module, uniqueId, false);
            }
        }
    }

    public void remove(ModuleInstance<?> moduleInstance) {
        this.removeByUniqueId(moduleInstance.getUniqueId(), true);
    }

    public void removeByUniqueId(UUID uniqueId, boolean sendUpdate) {
        ModuleInstance<?> moduleInstance = this.byUUID.remove(uniqueId);
        if (moduleInstance != null) {
            byModule.get(moduleInstance.getModule()).remove(moduleInstance);
            byModuleType.get(moduleInstance.getModuleType()).remove(moduleInstance);
            if (sendUpdate) {
                TransportAPI.getNetworkHandler().sendModuleCaseUpdate(modularEntity, moduleInstance, false);
            }
        }
    }
}
