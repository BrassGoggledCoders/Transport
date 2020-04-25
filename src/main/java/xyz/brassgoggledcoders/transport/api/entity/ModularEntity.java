package xyz.brassgoggledcoders.transport.api.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.TransportObjects;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlots;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModularEntity<ENT extends Entity & IItemProvider> implements IModularEntity {
    private final ENT entity;
    private final ImmutableList<ModuleSlot> moduleSlots;
    private final Map<ModuleSlot, ModuleInstance<?>> byModuleSlot;
    private final Map<ModuleType<?>, ModuleInstance<?>> byModuleType;

    public ModularEntity(ENT entity, ModuleSlot... moduleSlots) {
        this.entity = entity;
        this.moduleSlots = ImmutableList.copyOf(moduleSlots);
        this.byModuleSlot = Maps.newHashMap();
        this.byModuleType = Maps.newHashMap();
    }

    @Override
    public void openContainer(PlayerEntity playerEntity, INamedContainerProvider provider,
                              Consumer<PacketBuffer> packetBufferConsumer) {
        if (playerEntity instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, provider, packetBuffer -> {
                packetBuffer.writeInt(this.entity.getEntityId());
                packetBufferConsumer.accept(packetBuffer);
            });
        }
    }

    @Nonnull
    @Override
    public Entity getSelf() {
        return entity;
    }

    @Override
    public boolean canEquip(Module<?> module) {
        return this.getModuleInstance(module.getType()) == null;
    }

    @Override
    @Nonnull
    public List<ModuleSlot> getModuleSlots() {
        return moduleSlots;
    }

    @Override
    @Nullable
    public <T extends Module<T>> ModuleInstance<T> add(Module<T> module, ModuleSlot moduleSlot, boolean sendUpdate) {
        if (this.canEquip(module) && module.isValidFor(this) && !byModuleSlot.containsKey(moduleSlot)
                && this.getModuleSlots().contains(moduleSlot)) {
            ModuleInstance<T> moduleInstance = module.createInstance(this);
            byModuleSlot.put(moduleSlot, moduleInstance);
            byModuleType.put(moduleInstance.getModuleType(), moduleInstance);
            if (sendUpdate) {
                TransportAPI.getNetworkHandler().sendAddModuleCase(this, moduleInstance, moduleSlot);
            }
            return moduleInstance;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public ModuleInstance<?> getModuleInstance(ModuleSlot moduleSlot) {
        return byModuleSlot.get(moduleSlot);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module<T>, U extends ModuleInstance<T>> U getModuleInstance(ModuleType<T> moduleType) {
        return (U) byModuleType.get(moduleType);
    }

    @Override
    @Nonnull
    public Item asItem() {
        ItemStack itemStack = new ItemStack(this.entity.asItem());
        itemStack.getOrCreateTag().put("modules", this.serializeNBT());
        return this.entity.asItem();
    }

    @Override
    public ItemStack asItemStack() {
        ItemStack itemStack = new ItemStack(this.entity.asItem());
        itemStack.getOrCreateTag().put("modules", this.serializeNBT());
        return itemStack;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT caseNBT = new CompoundNBT();
        ListNBT moduleNBT = new ListNBT();
        for (Map.Entry<ModuleSlot, ModuleInstance<?>> entrySet : byModuleSlot.entrySet()) {
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
                ModuleInstance<?> moduleInstance = this.add(module, moduleSlot, false);
                if (moduleInstance != null) {
                    moduleInstance.deserializeNBT(instanceNBT);
                }
            }
        }
    }

    @Override
    public void write(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(byModuleSlot.size());
        for (Map.Entry<ModuleSlot, ModuleInstance<?>> entrySet : byModuleSlot.entrySet()) {
            Module.toPacketBuffer(entrySet.getValue().getModule(), packetBuffer);
            packetBuffer.writeString(entrySet.getKey().getName(), 64);
        }
    }

    @Override
    public Collection<ModuleInstance<?>> getModuleInstances() {
        return byModuleType.values();
    }

    @Override
    public void read(PacketBuffer packetBuffer) {
        byModuleSlot.clear();
        byModuleType.clear();
        int components = packetBuffer.readInt();
        for (int x = 0; x < components; x++) {
            Module<?> module = Module.fromPacketBuffer(packetBuffer);
            ModuleSlot moduleSlot = ModuleSlots.MODULE_SLOT_MAP.get(packetBuffer.readString(64));
            if (module != null && moduleSlot != null) {
                this.add(module, moduleSlot, false);
            }
        }
    }

    @Override
    public void remove(ModuleSlot moduleSlot, boolean sendUpdate) {
        ModuleInstance<?> moduleInstance = this.byModuleSlot.remove(moduleSlot);
        if (moduleInstance != null) {
            byModuleType.remove(moduleInstance.getModuleType());
            if (sendUpdate) {
                //TODO need send?
                //TransportAPI.getNetworkHandler().sendAddModuleCase(modularEntity, moduleInstance, false);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        CargoModuleInstance cargoModuleInstance = this.getModuleInstance(TransportObjects.CARGO_TYPE);
        if (cargoModuleInstance != null) {
            LazyOptional<T> lazyOptional = cargoModuleInstance.getCapability(cap, side);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return LazyOptional.empty();
    }
}
