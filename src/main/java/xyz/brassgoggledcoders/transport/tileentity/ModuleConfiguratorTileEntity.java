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
import com.hrznstudio.titanium.container.addon.SlotContainerAddon;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.item.IModularItem;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModuleCaseItemStackHandler;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.screen.addon.BasicSlotsScreenAddon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

public class ModuleConfiguratorTileEntity extends TileEntity implements IComponentHarness, IContainerAddonProvider,
        IScreenAddonProvider, INamedContainerProvider {
    private final InventoryComponent<IComponentHarness> modularItemInventory;
    private final ModuleCaseItemStackHandler moduleCaseItemStackHandler;

    private LazyOptional<IModularEntity> modularEntity;

    public ModuleConfiguratorTileEntity() {
        this(TransportBlocks.MODULE_CONFIGURATOR.getTileEntityType());
    }

    public ModuleConfiguratorTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.modularEntity = LazyOptional.empty();
        this.modularItemInventory = new InventoryComponent<>("modular_item", 116, 36, 1)
                .setComponentHarness(this)
                .setSlotLimit(1)
                .setOnSlotChanged(this::handleEntity);
        this.moduleCaseItemStackHandler = new ModuleCaseItemStackHandler(this::getModularEntity, this::saveItem);
    }

    private void saveItem(Void aVoid) {
        ItemStack itemStack = this.modularItemInventory.getStackInSlot(0);
        this.modularEntity.ifPresent(value -> itemStack.getOrCreateTag().put("modules", value.serializeNBT()));
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
            modularEntity = LazyOptional.empty();
        } else {
            if (itemStack.getItem() instanceof IModularItem<?>) {
                Entity entity = ((IModularItem<?>) itemStack.getItem()).getEntityType().create(Objects.requireNonNull(this.world));
                CompoundNBT instanceNBT = itemStack.getChildTag("modules");
                if (entity != null) {
                    modularEntity = entity.getCapability(TransportAPI.MODULAR_ENTITY);
                    if (instanceNBT != null) {
                        modularEntity.ifPresent(value -> value.deserializeNBT(instanceNBT));
                    }
                }
            }
        }
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
        if (!referenced) {
            this.getTheWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> screenAddons = Lists.newArrayList();
        screenAddons.addAll(modularItemInventory.getScreenAddons());
        screenAddons.add(() -> new BasicSlotsScreenAddon(8, 72, 9, slotId -> Pair.of(slotId * 18, 0)));
        return screenAddons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddon = Lists.newArrayList();
        containerAddon.addAll(modularItemInventory.getContainerAddons());
        containerAddon.add(() -> new SlotContainerAddon(moduleCaseItemStackHandler, 8, 72, slotId ->
                Pair.of(slotId * 18, 0)));
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
                TransportContainers.MODULE_CONFIGURATOR.get(), IWorldPosCallable.of(Objects.requireNonNull(this.world),
                this.pos), playerInventory, windowId);
    }

    @Nonnull
    public LazyOptional<IModularEntity> getModularEntity() {
        return this.modularEntity;
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT compoundNBT = super.write(compound);
        compoundNBT.put("modularInventory", this.modularItemInventory.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        this.modularItemInventory.deserializeNBT(compound.getCompound("modularInventory"));
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = super.getUpdateTag();
        compoundNBT.put("modularInventory", this.modularItemInventory.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.modularItemInventory.deserializeNBT(tag.getCompound("modularInventory"));
        this.handleEntity(this.modularItemInventory.getStackInSlot(0), 0);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT compoundNBT = pkt.getNbtCompound();
        this.handleUpdateTag(compoundNBT);
    }
}
