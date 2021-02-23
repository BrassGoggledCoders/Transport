package xyz.brassgoggledcoders.transport.screen.module;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.module.ChestModuleContainer;

public class ChestModuleScreen extends ModuleScreen<ChestModuleContainer> {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    public ChestModuleScreen(IModularScreen modularScreen, ChestModuleContainer moduleContainer) {
        super(modularScreen, moduleContainer);
        this.ySize = 114 + moduleContainer.getRows() * 18;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    @Override
    public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.getModuleContainer().getRows() * 18 + 17);
        this.blit(matrixStack, i, j + this.getModuleContainer().getRows() * 18 + 17, 0, 126,
                this.xSize, 96);
    }
}
