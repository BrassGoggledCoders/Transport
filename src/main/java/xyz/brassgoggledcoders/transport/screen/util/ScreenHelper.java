package xyz.brassgoggledcoders.transport.screen.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;

public class ScreenHelper {
    @SuppressWarnings("deprecation")
    public static void renderBackground(ContainerScreen<?> screen, ResourceLocation texture, MatrixStack matrixStack) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        screen.getMinecraft().getTextureManager().bindTexture(texture);
        int i = (screen.width - screen.getXSize()) / 2;
        int j = (screen.height - screen.getYSize()) / 2;
        screen.blit(matrixStack, i, j, 0, 0, screen.getXSize(), screen.getYSize());
        RenderSystem.popMatrix();
    }
}
