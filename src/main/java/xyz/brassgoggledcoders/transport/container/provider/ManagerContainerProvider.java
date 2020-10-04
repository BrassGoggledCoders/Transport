package xyz.brassgoggledcoders.transport.container.provider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.api.manager.IManager;
import xyz.brassgoggledcoders.transport.container.ManagerContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

public class ManagerContainerProvider implements INamedContainerProvider {
    private final ITextComponent displayName;
    private final IManager manager;

    public ManagerContainerProvider(ITextComponent displayName, IManager manager) {
        this.displayName = displayName;
        this.manager = manager;
    }

    public ManagerContainerProvider(TileEntity tileEntity, IManager manager) {
        this(new TranslationTextComponent(tileEntity.getBlockState()
                        .getBlock()
                        .getTranslationKey())
                        .mergeStyle(TextFormatting.BLACK),
                manager
        );
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return displayName;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ManagerContainer(windowId, inventory, new ArrayList<>(manager.getWorkerRepresentatives()));
    }
}
