package xyz.brassgoggledcoders.transport.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class BasicWidget extends Widget {
    public BasicWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }
}
