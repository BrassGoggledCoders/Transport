package xyz.brassgoggledcoders.transport.screen.navigation;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.navigation.NavigationChartContainer;

import javax.annotation.Nonnull;

public class NavigationChartScreen extends ContainerScreen<NavigationChartContainer> {
    private static final ResourceLocation NAVIGATION_CHART_TEXTURES = Transport.rl("textures/screen/navigation_chart.png");

    public NavigationChartScreen(NavigationChartContainer container, PlayerInventory playerInventory,
                                 ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.getMinecraft().getTextureManager().bindTexture(NAVIGATION_CHART_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        RenderSystem.disableBlend();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
    }
}
