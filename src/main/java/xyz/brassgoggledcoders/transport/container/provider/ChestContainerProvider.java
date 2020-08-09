package xyz.brassgoggledcoders.transport.container.provider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.brassgoggledcoders.transport.capability.itemhandler.InventoryWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

public class ChestContainerProvider implements INamedContainerProvider {
    private final IItemHandlerModifiable iItemHandler;
    private final Predicate<PlayerEntity> canInteract;

    public ChestContainerProvider(IItemHandlerModifiable iItemHandler, Predicate<PlayerEntity> canInteract) {
        this.iItemHandler = iItemHandler;
        this.canInteract = canInteract;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.minecraft.chest");
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ChestContainer.createGeneric9X3(id, playerInventory, new InventoryWrapper(iItemHandler, canInteract));
    }
}
