package xyz.brassgoggledcoders.transport.container;

import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.tileentity.loader.BasicLoaderTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class LoaderContainerProvider implements INamedContainerProvider {
    private final BasicLoaderTileEntity<?> loaderTileEntity;

    public LoaderContainerProvider(BasicLoaderTileEntity<?> basicLoaderTileEntity) {
        this.loaderTileEntity = basicLoaderTileEntity;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(loaderTileEntity.getBlockState()
                .getBlock()
                .getTranslationKey())
                .applyTextStyle(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BasicAddonContainer(loaderTileEntity, new TileEntityLocatorInstance(loaderTileEntity.getPos()),
                IWorldPosCallable.of(loaderTileEntity.getTheWorld(), loaderTileEntity.getPos()), playerInventory, i);
    }
}
