package xyz.brassgoggledcoders.transport.tileentity;

import net.minecraft.block.BlockState;
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
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModularItemStackHandler;
import xyz.brassgoggledcoders.transport.container.moduleconfigurator.ModuleConfiguratorContainer;
import xyz.brassgoggledcoders.transport.loot.entry.ILootDrop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;

public class ModuleConfiguratorTileEntity extends TileEntity implements INamedContainerProvider, ILootDrop {
    private final ModularItemStackHandler modularItemStackHandler;
    private final LazyOptional<IItemHandler> lazyOptional;

    public ModuleConfiguratorTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.modularItemStackHandler = new ModularItemStackHandler(this::getTheWorld, this::onChange);
        this.lazyOptional = LazyOptional.of(() -> modularItemStackHandler);
    }

    public ActionResultType openScreen(PlayerEntity playerEntity) {
        if (!playerEntity.isCrouching()) {
            if (!this.getTheWorld().isRemote() && playerEntity instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, this);
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Nonnull
    public World getTheWorld() {
        return Objects.requireNonNull(this.world);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey())
                .mergeStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ModuleConfiguratorContainer(windowId, playerInventory, IWorldPosCallable.of(this.getTheWorld(),
                this.getPos()), this.modularItemStackHandler);
    }

    @Nonnull
    public LazyOptional<IModularEntity> getModularEntity() {
        return this.modularItemStackHandler.getModularEntity();
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT compoundNBT = super.write(compound);
        compoundNBT.put("modularInventory", this.modularItemStackHandler.serializeNBT());
        return compoundNBT;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        this.modularItemStackHandler.deserializeNBT(compound.getCompound("modularInventory"));
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = super.getUpdateTag();
        compoundNBT.put("modularInventory", this.modularItemStackHandler.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.modularItemStackHandler.deserializeNBT(tag.getCompound("modularInventory"));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT compoundNBT = pkt.getNbtCompound();
        this.handleUpdateTag(this.getBlockState(), compoundNBT);
    }

    public void onChange() {
        this.getTheWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
                Constants.BlockFlags.DEFAULT);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.lazyOptional.cast();
        } else {
            return super.getCapability(capability, side);
        }
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.lazyOptional.invalidate();
    }

    @Override
    public void onLootDrop(Consumer<ItemStack> itemStackConsumer) {
        this.modularItemStackHandler.forEach(itemStackConsumer);
    }
}
