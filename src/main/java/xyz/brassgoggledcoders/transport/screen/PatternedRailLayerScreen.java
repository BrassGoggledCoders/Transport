package xyz.brassgoggledcoders.transport.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.menu.PatternedRailLayerMenu;

public class PatternedRailLayerScreen extends AbstractContainerScreen<PatternedRailLayerMenu> implements MenuAccess<PatternedRailLayerMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation POINTER_LOCATION = new ResourceLocation("textures/gui/server_selection.png");
    private final int containerRows;

    public PatternedRailLayerScreen(PatternedRailLayerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.containerRows = pMenu.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderPosition(pPoseStack);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pX, int pY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        guiGraphics.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    protected void renderPosition(@NotNull GuiGraphics guiGraphics) {
        RenderSystem.setShaderTexture(0, POINTER_LOCATION);
        int i = (this.width - this.imageWidth);
        int j = (this.height - this.imageHeight);
        //this.setBlitOffset(300);
        //guiGraphics.pushPose();
        //guiGraphics.scale(0.5F, 0.5F, 0.5F);
        int xOffset = this.getMenu().getPosition() * 36;
        //guiGraphics.blitSprite(POINTER_LOCATION, i + 25 + xOffset, j + 68, 99, 5, 11, 7);
        //guiGraphics.popPose();
        //guiGraphics.setBlitOffset(0);
    }
}