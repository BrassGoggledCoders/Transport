package xyz.brassgoggledcoders.transport.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;

public class PatternedRailLayerScreen extends AbstractContainerScreen<PatternedRailLayerMenu> implements MenuAccess<PatternedRailLayerMenu> {
    /** The ResourceLocation containing the chest GUI texture. */
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    /** Window height is calculated with these values" the more rows, the higher */
    private final int containerRows;

    public PatternedRailLayerScreen(PatternedRailLayerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.passEvents = false;
        this.containerRows = pMenu.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderPosition(pPoseStack);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        this.blit(pPoseStack, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    protected void renderPosition(@NotNull PoseStack pPoseStack) {
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/server_selection.png"));
        int i = (this.width - this.imageWidth);
        int j = (this.height - this.imageHeight);
        this.setBlitOffset(300);
        pPoseStack.pushPose();
        pPoseStack.scale(0.5F, 0.5F, 0.5F);
        int xOffset = this.getMenu().getPosition() * 36;
        this.blit(pPoseStack, i + 25 + xOffset, j + 68, 99, 5, 11, 7);
        pPoseStack.popPose();
        this.setBlitOffset(0);
    }
}