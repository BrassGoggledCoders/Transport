package xyz.brassgoggledcoders.transport.screen.module;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.module.VehicleModuleContainer;
import xyz.brassgoggledcoders.transport.container.slot.ModuleSlotSlot;

import javax.annotation.Nonnull;

public class VehicleModuleScreen extends ContainerScreen<VehicleModuleContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/module_configurator.png");

    public VehicleModuleScreen(VehicleModuleContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        for (Slot slot : this.getContainer().inventorySlots) {
            if (slot instanceof ModuleSlotSlot && slot.isEnabled()) {
                int xPos = this.guiLeft + slot.xPos - 1;
                int yPos = this.guiTop + slot.yPos - 1;
                this.blit(matrixStack, xPos, yPos, 7, 83, 18, 18);
            }
        }

        RenderSystem.popMatrix();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        for (Slot slot : this.getContainer().inventorySlots) {
            if (slot instanceof ModuleSlotSlot && slot.isEnabled()) {
                int xPos = slot.xPos - 1;
                int yPos = slot.yPos - 1;
                this.font.func_243248_b(matrixStack, ((ModuleSlotSlot) slot).getModuleSlot().getDisplayName(),
                        xPos + 20, yPos + 5, 4210752);

            }
        }
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
