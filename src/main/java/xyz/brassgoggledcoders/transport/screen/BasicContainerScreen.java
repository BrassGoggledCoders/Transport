package xyz.brassgoggledcoders.transport.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class BasicContainerScreen<T extends Container> extends ContainerScreen<T> {
    public BasicContainerScreen(T screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
