package xyz.brassgoggledcoders.transport.container.provider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.container.YardMasterContainer;
import xyz.brassgoggledcoders.transport.tileentity.YardMasterTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class YardMasterContainerProvider implements INamedContainerProvider {
    private final YardMasterTileEntity tileEntity;

    public YardMasterContainerProvider(YardMasterTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(tileEntity.getBlockState()
                .getBlock()
                .getTranslationKey())
                .mergeStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new YardMasterContainer(windowId, inventory, tileEntity.getConnectedObjects());
    }
}
