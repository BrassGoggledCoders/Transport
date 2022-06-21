package xyz.brassgoggledcoders.transport.shellcontent.storage.item;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.builtin.StorageSize;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemStorageShellContent extends ShellContent implements MenuProvider {
    private final StorageSize size;
    private final boolean showScreen;

    private final ItemStackHandler itemStackHandler;
    private final LazyOptional<IItemHandler> itemLazyOptional;

    public ItemStorageShellContent(StorageSize size, boolean showScreen) {
        this.size = size;
        this.showScreen = showScreen;
        this.itemStackHandler = new ItemStackHandler(size.getTotal());
        this.itemLazyOptional = LazyOptional.of(this::getHandler);
    }

    @Nonnull
    private IItemHandler getHandler() {
        return this.itemStackHandler;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.itemLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    @Nonnull
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (this.showScreen) {
            InteractionResult ret = super.interact(pPlayer, pHand);
            if (ret.consumesAction()) {
                return ret;
            }
            pPlayer.openMenu(this);
            if (!pPlayer.level.isClientSide) {
                this.getLevel().gameEvent(GameEvent.CONTAINER_OPEN, pPlayer);
                PiglinAi.angerNearbyPiglins(pPlayer, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return this.getShell().getSelf().getName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return showScreen ? size.createMenu(
                pContainerId,
                pInventory,
                new CapabilityContainer(
                        this.itemStackHandler,
                        this::stillValid
                )
        ) : null;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemLazyOptional.invalidate();
    }
}
