package xyz.brassgoggledcoders.transport.screen.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class FluidRenderer {
    @SuppressWarnings("deprecation")
    public static void renderFluid(MatrixStack matrixStack, FluidStack fluidStack, int capacity, int x, int y, int width, int height) {
        if (!fluidStack.isEmpty()) {
            int stored = fluidStack.getAmount();
            int offset = stored * height / capacity;

            FluidAttributes fluidAttributes = fluidStack.getFluid()
                    .getAttributes();
            ResourceLocation flowing = fluidAttributes.getStillTexture(fluidStack);
            if (flowing != null) {
                TextureAtlasSprite flowingSprite = Minecraft.getInstance()
                        .getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
                        .apply(flowing);
                Minecraft.getInstance()
                        .getTextureManager()
                        .bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

                Color color = new Color(fluidStack.getFluid()
                        .getAttributes()
                        .getColor());

                RenderSystem.color4f(
                        (float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F,
                        (float) color.getBlue() / 255.0F,
                        (float) color.getAlpha() / 255.0F
                );
                RenderSystem.enableBlend();
                int startY = y + (fluidAttributes.isGaseous() ? 0 : height - offset);
                Screen.blit(matrixStack, x, startY, 0, width, offset, flowingSprite);
                RenderSystem.disableBlend();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
