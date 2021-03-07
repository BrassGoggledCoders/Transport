package xyz.brassgoggledcoders.transport.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IModularEntity extends IItemProvider, INBTSerializable<CompoundNBT>, ICapabilityProvider {
    void openContainer(PlayerEntity playerEntity, INamedContainerProvider provider, Consumer<PacketBuffer> packetBufferConsumer);

    default boolean canInteractWith(PlayerEntity playerEntity) {
        return this.getSelf().isAlive() && playerEntity.getDistanceSq(this.getSelf()) < 64.0D;
    }

    @Nonnull
    default World getTheWorld() {
        return this.getSelf().getEntityWorld();
    }

    @Nonnull
    Entity getSelf();

    boolean canEquip(Module<?> module);

    @Nonnull
    List<ModuleSlot> getModuleSlots();

    void remove(ModuleSlot moduleSlot, boolean sendUpdate);

    void remove(ModuleInstance<?> moduleInstance);

    @Nullable
    <T extends Module<T>> ModuleInstance<T> add(Module<T> module, ModuleSlot moduleSlot, boolean sendUpdate);

    @Nullable
    ModuleInstance<?> getModuleInstance(ModuleSlot moduleSlot);

    @Nullable
    <T extends Module<T>, U extends ModuleInstance<T>> U getModuleInstance(ModuleType moduleType);

    @Nullable
    default <T extends Module<T>, U extends ModuleInstance<T>> U getModuleInstance(Supplier<ModuleType> moduleType) {
        return getModuleInstance(moduleType.get());
    }

    default <T extends Module<T>, U extends ModuleInstance<T>, V> V callModule(Supplier<ModuleType> moduleType,
                                                                               Function<U, V> calling, Supplier<V> defaultValue) {
        U moduleInstance = this.getModuleInstance(moduleType);
        if (moduleInstance != null) {
            return calling.apply(moduleInstance);
        } else {
            return defaultValue.get();
        }
    }

    @Nonnull
    ItemStack asItemStack();

    void read(PacketBuffer packetBuffer);

    void write(PacketBuffer packetBuffer);

    @Nonnull
    Collection<ModuleInstance<?>> getModuleInstances();

    default ActionResultType applyPlayerInteraction(Supplier<ModuleSlot> moduleSlot, PlayerEntity player, Vector3d vec, Hand hand) {
        return this.applyPlayerInteraction(moduleSlot.get(), player, vec, hand);
    }

    ActionResultType applyPlayerInteraction(ModuleSlot moduleSlot, PlayerEntity player, Vector3d vec, Hand hand);

    <T> List<LazyOptional<T>> getCapabilities(@Nonnull Capability<T> cap, @Nullable Direction side, @Nullable ModuleSlot priority);

    void invalidateCapabilities();

    void sendClientUpdate(ModuleInstance<?> moduleInstance, int type, CompoundNBT compoundNBT);

    void openModuleContainer(ModuleInstance<?> moduleInstance, PlayerEntity playerEntity);

    List<ModuleTab> getModuleTabs();
}
