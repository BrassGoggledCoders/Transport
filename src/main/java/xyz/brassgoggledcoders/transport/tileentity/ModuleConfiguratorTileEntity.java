package xyz.brassgoggledcoders.transport.tileentity;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlots;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class ModuleConfiguratorTileEntity extends TileEntity implements IComponentHarness, IContainerAddonProvider,
        IScreenAddonProvider, INamedContainerProvider {
    private final InventoryComponent<IComponentHarness> modularItemInventory;
    private final InventoryComponent<IComponentHarness> moduleInventory;
    private final boolean[] valid;

    private IModularEntity entity;

    public ModuleConfiguratorTileEntity() {
        this(TransportBlocks.MODULE_CONFIGURATOR.getTileEntityType());
    }

    public ModuleConfiguratorTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.modularItemInventory = new InventoryComponent<>("modular_item", 116, 36, 1)
                .setComponentHarness(this)
                .setOnSlotChanged(this::handleEntity);
        this.moduleInventory = new InventoryComponent<>("module", 8, 72, 9)
                .setInputFilter(this::isValidModule)
                .setOnSlotChanged(this::handleModule);
        this.valid = new boolean[9];
    }

    private void handleModule(ItemStack itemStack, Integer slotId) {
        if (!itemStack.isEmpty() && entity != null) {
            valid[slotId] = trySetModule(entity, itemStack, slotId);
        } else {
            if (entity != null) {
                entity.getModuleCase().removeByModuleSlot(this.getModuleSlotFor(slotId));
                updateModularItem();
            }
            valid[slotId] = false;
        }
    }

    private void updateModularItem() {
        ItemStack modularItemStack = modularItemInventory.getStackInSlot(0);
        if (entity != null && !modularItemStack.isEmpty()) {
            modularItemStack.getOrCreateTag().put("modules", entity.getModuleCase().serializeNBT());
        }
    }

    private boolean isValidModule(ItemStack itemStack, Integer slotId) {
        if (entity != null && !itemStack.isEmpty()) {
            Module<?> module = TransportAPI.getModuleFromItem(itemStack.getItem());
            ModuleSlot moduleSlot = this.getModuleSlotFor(slotId);
            if (module != null) {
                return moduleSlot.isModuleValid(entity, module) && entity.canEquipModule(module) && module.isValidFor(entity);
            }
        }
        return false;
    }

    public ActionResultType openScreen(PlayerEntity playerEntity) {
        if (!playerEntity.isCrouching()) {
            if (!this.getTheWorld().isRemote() && playerEntity instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, this, packetBuffer ->
                        LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.pos)));
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    private void handleEntity(ItemStack itemStack, Integer integer) {
        if (itemStack.isEmpty()) {
            entity = null;
            for (int x = 0; x < valid.length; x++) {
                if (valid[x]) {
                    this.moduleInventory.setStackInSlot(x, ItemStack.EMPTY);
                }
                valid[x] = false;
            }
        } else {
            if (itemStack.getItem() instanceof IModularItem<?>) {
                entity = ((IModularItem<?>) itemStack.getItem()).getEntityType().create(Objects.requireNonNull(this.world));
                CompoundNBT instanceNBT = itemStack.getChildTag("modules");
                if (entity != null) {
                    if (instanceNBT != null) {
                        entity.getModuleCase().deserializeNBT(instanceNBT);
                    }

                    for (int slotId = 0; slotId < moduleInventory.getSlots(); slotId++) {
                        ModuleSlot moduleSlot = this.getModuleSlotFor(slotId);
                        ModuleInstance<?> moduleInstance = entity.getModuleCase().getByModuleSlot(moduleSlot);
                        ItemStack currentStack = moduleInventory.getStackInSlot(slotId);
                        if (moduleInstance != null) {
                            if (!currentStack.isEmpty()) {
                                this.getTheWorld().addEntity(new ItemEntity(this.getTheWorld(), this.getPos().getX(),
                                        this.getPos().getY() + 1, this.getPos().getZ(), currentStack));
                            }
                            moduleInventory.setStackInSlot(slotId, new ItemStack(moduleInstance.getModule().asItem()));
                            valid[slotId] = true;
                        } else {
                            if (!currentStack.isEmpty()) {
                                valid[slotId] = trySetModule(entity, currentStack, slotId);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean trySetModule(IModularEntity entity, ItemStack itemStack, int slotId) {
        Module<?> module = TransportAPI.getModuleFromItem(itemStack.getItem());
        if (module != null) {
            ModuleInstance<?> moduleInstance = entity.getModuleCase().addModule(module, this.getModuleSlotFor(slotId), false);
            updateModularItem();
            return moduleInstance != null;
        } else {
            return false;
        }
    }

    @Nonnull
    public ModuleSlot getModuleSlotFor(int slotId) {
        if (entity != null) {
            List<ModuleSlot> moduleSlots = entity.getModuleCase().getModuleSlots();
            if (slotId < moduleSlots.size()) {
                return moduleSlots.get(slotId);
            }
        }
        return ModuleSlots.NONE;
    }

    @Nonnull
    public World getTheWorld() {
        return Objects.requireNonNull(this.world);
    }

    @Override
    public World getComponentWorld() {
        return this.world;
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {

    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> screenAddons = Lists.newArrayList();
        screenAddons.addAll(modularItemInventory.getScreenAddons());
        screenAddons.addAll(moduleInventory.getScreenAddons());
        return screenAddons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddon = Lists.newArrayList();
        containerAddon.addAll(modularItemInventory.getContainerAddons());
        containerAddon.addAll(moduleInventory.getContainerAddons());
        return containerAddon;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey())
                .applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.pos),
                IWorldPosCallable.of(Objects.requireNonNull(this.world), this.pos), playerInventory, windowId);
    }

    @Nullable
    public IModularEntity getEntity() {
        return this.entity;
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT compoundNBT = super.write(compound);
        compoundNBT.put("modularInventory", this.modularItemInventory.serializeNBT());
        compoundNBT.put("moduleInventory", this.moduleInventory.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        this.modularItemInventory.deserializeNBT(compound.getCompound("modularInventory"));
        this.moduleInventory.deserializeNBT(compound.getCompound("moduleInventory"));
    }
}
