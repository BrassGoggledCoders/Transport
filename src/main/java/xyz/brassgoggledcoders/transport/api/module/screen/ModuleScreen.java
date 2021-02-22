package xyz.brassgoggledcoders.transport.api.module.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;

import javax.annotation.Nullable;
import java.util.List;

public class ModuleScreen<T extends ModuleContainer> {
    private final T moduleContainer;
    private final IModularScreen modularScreen;

    private FontRenderer fontRenderer;

    protected int xSize = 176;
    protected int ySize = 166;

    public ModuleScreen(IModularScreen modularScreen, T moduleContainer) {
        this.moduleContainer = moduleContainer;
        this.modularScreen = modularScreen;
    }

    public T getModuleContainer() {
        return this.moduleContainer;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    @SuppressWarnings("unused")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

    }

    public void blit(MatrixStack matrixStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        Screen.blit(matrixStack, x, y, 0, (float) uOffset, (float) vOffset, uWidth, vHeight, 256, 256);
    }

    public void drawForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.getFontRenderer().func_243248_b(
                matrixStack,
                this.getModuleContainer()
                        .getModularContainer()
                        .getActiveTab()
                        .getDisplayName(),
                8F,
                6F,
                4210752
        );
        this.getFontRenderer().func_243248_b(
                matrixStack,
                this.getPlayerInventory().getDisplayName(),
                8F,
                (float) this.getYSize() - 94,
                4210752
        );

    }

    @SuppressWarnings("unused")
    public void init(Minecraft minecraft, int width, int height) {
        this.fontRenderer = minecraft.fontRenderer;
    }

    public void init() {

    }

    @Nullable
    public ResourceLocation getBackgroundTexture() {
        return null;
    }

    protected IModularScreen getModularScreen() {
        return this.modularScreen;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public List<Slot> getInventorySlots() {
        return this.getModuleContainer()
                .getModularContainer()
                .asContainer()
                .inventorySlots;
    }

    public PlayerInventory getPlayerInventory() {
        return this.getModuleContainer()
                .getModularContainer()
                .getPlayerInventory();
    }
}
