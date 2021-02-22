package xyz.brassgoggledcoders.transport.screen.modular;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.modular.ModularContainer;

import javax.annotation.Nonnull;

public class ModularScreen extends ContainerScreen<ModularContainer> implements IModularScreen {
    private ModuleScreen<?> activeScreen;
    private ModuleContainer lastActiveContainer;

    public ModularScreen(ModularContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        checkActiveScreen();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.pushMatrix();
        ResourceLocation background = this.activeScreen.getBackgroundTexture();
        if (background != null) {
            this.getMinecraft().getTextureManager().bindTexture(background);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        }
        this.activeScreen.drawBackgroundLayer(matrixStack, partialTicks, x, y);
        RenderSystem.popMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.activeScreen.drawForegroundLayer(matrixStack, x, y);
    }

    @Override
    protected void init() {
        super.init();
        this.activeScreen.init();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        checkActiveScreen();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.activeScreen.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void init(@Nonnull Minecraft minecraft, int width, int height) {
        this.activeScreen.init(minecraft, width, height);
        super.init(minecraft, width, height);
    }

    private void checkActiveScreen() {
        ModularContainer container = this.getContainer();
        if (this.lastActiveContainer != container.getActiveContainer()) {
            this.lastActiveContainer = container.getActiveContainer();
            this.activeScreen = container.getActiveTab()
                    .getModuleScreenCreator()
                    .get()
                    .apply(this, container.getActiveContainer());
            this.xSize = this.activeScreen.getXSize();
            this.ySize = this.activeScreen.getYSize();
        }
    }

    @Override
    public int getLeft() {
        return this.getGuiLeft();
    }

    @Override
    public int getTop() {
        return this.getGuiTop();
    }
}
