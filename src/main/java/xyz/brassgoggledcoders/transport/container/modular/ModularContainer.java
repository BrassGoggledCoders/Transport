package xyz.brassgoggledcoders.transport.container.modular;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.container.IModularContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.network.property.*;
import xyz.brassgoggledcoders.transport.screen.modular.BlankModuleScreen;
import xyz.brassgoggledcoders.transport.screen.modular.VehicleModuleScreen;
import xyz.brassgoggledcoders.transport.util.WorldHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModularContainer extends Container implements IModularContainer, IPropertyManaged, IHost<ModuleContainer> {
    private static final ModuleTab<BlankModuleContainer> blankTab = new ModuleTab<>(
            TransportText.BLANK,
            () -> ItemStack.EMPTY,
            BlankModuleContainer::new,
            () -> BlankModuleScreen::new
    );

    private final IWorldPosCallable worldPosCallable;
    private final PlayerInventory playerInventory;
    private final LazyOptional<IModularEntity> modularEntityLazy;
    private final ModuleTab<VehicleModuleContainer> vehicleTab;
    private final Map<ModuleInstance<?>, ModuleTab<?>> existingModuleTabs;

    private final PropertyManager propertyManager;
    private final Property<Integer> activeTabIndex;

    private ModuleTab<?> activeTab;
    private ModuleContainer activeContainer;

    public ModularContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory,
                            IWorldPosCallable worldPosCallable, LazyOptional<IModularEntity> modularEntityLazy) {
        super(type, id);
        this.playerInventory = playerInventory;
        this.worldPosCallable = worldPosCallable;
        this.modularEntityLazy = modularEntityLazy;
        this.vehicleTab = modularEntityLazy.map(modularEntity ->
                new ModuleTab<>(
                        modularEntity.getSelf().getDisplayName(),
                        modularEntity::asItemStack,
                        VehicleModuleContainer::new,
                        () -> VehicleModuleScreen::new
                )
        ).orElse(null);
        if (this.vehicleTab != null) {
            this.setActiveTab(vehicleTab);
        } else {
            this.setActiveTab(blankTab);
        }
        this.existingModuleTabs = this.modularEntityLazy.map(modularEntity -> {
            Map<ModuleInstance<?>, ModuleTab<?>> tabs = Maps.newHashMap();
            for (ModuleInstance<?> moduleInstance : modularEntity.getModuleInstances()) {
                tabs.put(moduleInstance, moduleInstance.createTab());
            }
            tabs.values().removeIf(Objects::isNull);
            return tabs;
        }).orElseGet(Maps::newHashMap);

        this.propertyManager = new PropertyManager((short) id);
        this.activeTabIndex = this.propertyManager.addTrackedProperty(PropertyTypes.INTEGER.create(
                this::getActiveTabIndex,
                this::setActiveTabIndex
        ));
    }

    public void setActiveTab(ModuleTab<?> moduleTab) {
        if (this.activeTab != moduleTab) {
            this.activeTab = moduleTab;
            this.inventorySlots.clear();
            this.activeContainer = moduleTab.getModuleContainerCreator().apply(this);
            this.activeContainer.setup();
        }
    }

    public void setActiveTab(ModuleInstance<?> moduleInstance) {
        ModuleTab<?> moduleTab = existingModuleTabs.get(moduleInstance);
        if (moduleTab != null) {
            this.setActiveTab(moduleTab);
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.modularEntityLazy.isPresent() &&
                worldPosCallable.applyOrElse(WorldHelper.isPlayerNear(player)::test, true);
    }

    @Nonnull
    public List<ModuleTab<?>> getExistingModuleTabs() {
        List<ModuleTab<?>> tabs = Lists.newArrayList();

        this.modularEntityLazy.ifPresent(modularEntity -> {
            Map<ModuleInstance<?>, ModuleTab<?>> currentTabs = Maps.newHashMap();
            for (ModuleInstance<?> moduleInstance : modularEntity.getModuleInstances()) {
                if (existingModuleTabs.containsKey(moduleInstance)) {
                    currentTabs.put(moduleInstance, existingModuleTabs.get(moduleInstance));
                } else {
                    currentTabs.put(moduleInstance, moduleInstance.createTab());
                }
            }
            currentTabs.values().removeIf(Objects::isNull);
            existingModuleTabs.clear();
            existingModuleTabs.putAll(currentTabs);
            tabs.addAll(existingModuleTabs.values());
        });
        tabs.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(
                o1.getDisplayName().getString(),
                o2.getDisplayName().getString()
        ));
        if (vehicleTab != null) {
            tabs.add(0, vehicleTab);
        }
        return tabs;
    }

    @Nonnull
    public ModuleTab<?> getActiveTab() {
        if (activeTab != null) {
            return activeTab;
        } else if (vehicleTab != null) {
            return vehicleTab;
        } else {
            return blankTab;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ModuleContainer> T getActiveContainer() {
        return (T) activeContainer;
    }

    @Override
    public short getId() {
        return (short) windowId;
    }

    @Override
    public void putSlot(Slot slot) {
        this.addSlot(slot);
    }

    @Override
    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    @Override
    public Container asContainer() {
        return this;
    }

    @Override
    public boolean attemptMergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return this.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
    }

    @Override
    public LazyOptional<IModularEntity> getModularEntity() {
        return modularEntityLazy;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int index) {
        return this.activeContainer.transferStackInSlot(player, index);
    }

    private int getActiveTabIndex() {
        List<ModuleTab<?>> tabs = this.getExistingModuleTabs();
        ModuleTab<?> activeTab = this.getActiveTab();
        int i = 0;
        for (ModuleTab<?> tab : tabs) {
            if (tab == activeTab) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void setActiveTabIndex(int tabIndex) {
        if (tabIndex >= 0) {
            List<ModuleTab<?>> tabs = this.getExistingModuleTabs();
            if (tabIndex < tabs.size()) {
                ModuleTab<?> moduleTab = tabs.get(tabIndex);
                this.setActiveTab(moduleTab);
            }
        }
    }

    public Property<Integer> getActiveTabIndexProperty() {
        return this.activeTabIndex;
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        this.propertyManager.sendChanges(Collections.singletonList(listener), true);
        this.activeContainer.addListener(listener);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.propertyManager.sendChanges(this.listeners, false);
        this.activeContainer.detectAndSendChanges(this.listeners);
    }

    @Override
    public ModuleContainer getHosted() {
        return this.activeContainer;
    }

    @Nonnull
    public static ModularContainer create(ContainerType<ModularContainer> containerType, int windowId,
                                          PlayerInventory playerInventory, @Nullable PacketBuffer packetBuffer) {
        if (packetBuffer != null) {
            int entityId = packetBuffer.readInt();
            Entity entity = playerInventory.player.getEntityWorld().getEntityByID(entityId);
            if (entity != null) {
                LazyOptional<IModularEntity> modularEntityLazy = entity.getCapability(TransportAPI.MODULAR_ENTITY);
                if (!modularEntityLazy.isPresent()) {
                    Transport.LOGGER.error("Failed to Find Modular Entity for Modular Container");
                } else {
                    ModularContainer modularContainer = new ModularContainer(
                            containerType,
                            windowId,
                            playerInventory,
                            IWorldPosCallable.DUMMY,
                            modularEntityLazy
                    );
                    if (packetBuffer.readBoolean()) {
                        Module<?> module = Module.fromPacketBuffer(packetBuffer);
                        if (module != null) {
                            modularEntityLazy.resolve()
                                    .<ModuleInstance<?>>map(modularEntity -> modularEntity.getModuleInstance(module.getType()))
                                    .ifPresent(modularContainer::setActiveTab);
                        }
                    }
                    return modularContainer;
                }
            } else {
                Transport.LOGGER.error("Failed to Find Entity for Modular Container");
            }
        } else {
            Transport.LOGGER.error("No PacketBuffer received for Modular Container");
        }
        return new ModularContainer(containerType, windowId, playerInventory, IWorldPosCallable.DUMMY, LazyOptional.empty());
    }
}
