package xyz.brassgoggledcoders.transport.screen.locomotive;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;

import javax.annotation.Nonnull;

public class SteamLocomotiveScreen extends ContainerScreen<SteamLocomotiveContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/steam_locomotive.png");

    public SteamLocomotiveScreen(SteamLocomotiveContainer screenContainer, PlayerInventory playerInventory,
                                 ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if (!this.getContainer().isLocomotiveOn()) {
            this.blit(matrixStack, i + 73, j + 36, 210, 30, 12, 12);
        }

        int burnedAmount = this.getContainer().getFuelBurnedScaled();
        if (burnedAmount > 0) {
            this.blit(matrixStack, i + 76, j + 55, 184, 15, 7, burnedAmount);
        }

        this.blit(matrixStack, i + 30, j + 19, 177, 31, 20, 51);

        matrixStack.push();
        matrixStack.translate(i + 127, j + 15, 0);
        this.blit(matrixStack, 0, 0, 230, 8, 7, 46);
        matrixStack.pop();
        RenderSystem.popMatrix();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
