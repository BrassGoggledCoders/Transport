package xyz.brassgoggledcoders.transport.screen.loader;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.loader.FluidLoaderContainer;
import xyz.brassgoggledcoders.transport.screen.BasicContainerScreen;
import xyz.brassgoggledcoders.transport.screen.util.ScreenHelper;
import xyz.brassgoggledcoders.transport.screen.widget.FluidTankWidget;

import javax.annotation.Nonnull;

public class FluidLoaderScreen extends BasicContainerScreen<FluidLoaderContainer> {
    private final static ResourceLocation BACKGROUND = Transport.rl("textures/screen/blank_with_player.png");

    public FluidLoaderScreen(FluidLoaderContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new FluidTankWidget(
                this.getGuiLeft() + 79,
                this.getGuiTop() + 15,
                this.getContainer()::getFluidStack,
                this.getContainer()::getTankSize
        ));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        ScreenHelper.renderBackground(this, BACKGROUND, matrixStack);
    }
}
