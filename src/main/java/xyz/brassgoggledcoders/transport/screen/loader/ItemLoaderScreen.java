package xyz.brassgoggledcoders.transport.screen.loader;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.loader.ItemLoaderContainer;
import xyz.brassgoggledcoders.transport.screen.BasicContainerScreen;
import xyz.brassgoggledcoders.transport.screen.util.ScreenHelper;

import javax.annotation.Nonnull;

public class ItemLoaderScreen extends BasicContainerScreen<ItemLoaderContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/item_loader.png");

    public ItemLoaderScreen(ItemLoaderContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        ScreenHelper.renderBackground(this, BACKGROUND, matrixStack);
    }
}
