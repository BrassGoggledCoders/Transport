package xyz.brassgoggledcoders.transport.screen.module.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.module.engine.SolidFuelModuleContainer;

import javax.annotation.Nullable;

public class SolidFuelModuleScreen extends ModuleScreen<SolidFuelModuleContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/solid_fuel.png");

    public SolidFuelModuleScreen(IModularScreen modularScreen, SolidFuelModuleContainer moduleContainer) {
        super(modularScreen, moduleContainer);
    }

    @Override
    public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawBackgroundLayer(matrixStack, partialTicks, x, y);
        int burnTimeScaled = this.getModuleContainer().getFuelBurnedScaled();
        if (burnTimeScaled > 0) {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(matrixStack, i + 81, j + 38 - burnTimeScaled, 176, 14 - burnTimeScaled, 14, burnTimeScaled);
        }
    }

    @Nullable
    @Override
    public ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }
}
