package xyz.brassgoggledcoders.transport.screen.modular;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.modular.VehicleModuleContainer;
import xyz.brassgoggledcoders.transport.container.slot.ModuleSlotSlot;

import javax.annotation.Nullable;

public class VehicleModuleScreen extends ModuleScreen<VehicleModuleContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/module_configurator.png");

    public VehicleModuleScreen(IModularScreen modularScreen, VehicleModuleContainer moduleContainer) {
        super(modularScreen, moduleContainer);
    }

    @Override
    public void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawBackgroundLayer(matrixStack, partialTicks, x, y);
        for (Slot slot : this.getInventorySlots()) {
            if (slot instanceof ModuleSlotSlot && slot.isEnabled()) {
                int xPos = this.getModularScreen().getLeft() + slot.xPos - 1;
                int yPos = this.getModularScreen().getTop() + slot.yPos - 1;
                this.blit(matrixStack, xPos, yPos, 7, 83, 18, 18);
            }
        }
    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawForegroundLayer(matrixStack, x, y);
        for (Slot slot : this.getInventorySlots()) {
            if (slot instanceof ModuleSlotSlot && slot.isEnabled()) {
                int xPos = slot.xPos - 1;
                int yPos = slot.yPos - 1;
                this.getFontRenderer().func_243248_b(matrixStack, ((ModuleSlotSlot) slot).getModuleSlot().getDisplayName(),
                        xPos + 20, yPos + 5, 4210752);

            }
        }
    }

    @Nullable
    @Override
    public ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }
}
