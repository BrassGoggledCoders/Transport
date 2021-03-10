package xyz.brassgoggledcoders.transport.screen.module.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.module.engine.SolidFuelModuleContainer;
import xyz.brassgoggledcoders.transport.screen.BasicContainerScreen;
import xyz.brassgoggledcoders.transport.screen.util.ScreenHelper;

import javax.annotation.Nonnull;

public class SolidFuelModuleScreen extends BasicContainerScreen<SolidFuelModuleContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/solid_fuel.png");

    public SolidFuelModuleScreen(SolidFuelModuleContainer moduleContainer, PlayerInventory playerInventory,
                                 ITextComponent title) {
        super(moduleContainer, playerInventory, title);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        ScreenHelper.renderBackground(this, BACKGROUND, matrixStack);
        int burnTimeScaled = this.getContainer().getFuelBurnedScaled();
        if (burnTimeScaled > 0) {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(matrixStack, i + 81, j + 38 - burnTimeScaled, 176, 14 - burnTimeScaled, 14, burnTimeScaled);
        }
    }
}
