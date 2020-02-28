package xyz.brassgoggledcoders.transport.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.tileentity.loader.BasicLoaderTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class LoaderContainerProvider implements INamedContainerProvider {
    private final LoaderBlock loaderBlock;
    private final BasicLoaderTileEntity<?> loaderTileEntity;

    public LoaderContainerProvider(LoaderBlock loaderBlock, BasicLoaderTileEntity<?> basicLoaderTileEntity) {
        this.loaderBlock = loaderBlock;
        this.loaderTileEntity = basicLoaderTileEntity;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return loaderBlock.getDisplayName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new LoaderContainer(i, playerInventory, loaderTileEntity);
    }
}
