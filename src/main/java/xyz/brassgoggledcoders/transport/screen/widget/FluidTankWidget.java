package xyz.brassgoggledcoders.transport.screen.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.screen.util.FluidRenderer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

public class FluidTankWidget extends Widget implements IHoverableWidget {
    private final static ResourceLocation COMPONENTS = new ResourceLocation("titanium", "textures/gui/background.png");

    private final NonNullSupplier<FluidStack> fluidStack;
    private final IntSupplier tankSize;

    public FluidTankWidget(int x, int y, NonNullSupplier<FluidStack> fluidStack,
                           IntSupplier tankSize) {
        super(x, y, 18, 56, new StringTextComponent("Fluid Tank"));
        this.fluidStack = fluidStack;
        this.tankSize = tankSize;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (this.visible) {
            FluidStack renderStack = fluidStack.get();
            FluidRenderer.renderFluid(
                    matrixStack,
                    renderStack,
                    tankSize.getAsInt(),
                    x,
                    y,
                    width,
                    height
            );
            Minecraft.getInstance()
                    .getTextureManager()
                    .bindTexture(COMPONENTS);
            this.blit(matrixStack, x, y, 177, 1, width, height);
        }
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    @Nonnull
    @Override
    public List<? extends ITextProperties> getHoveredText() {
        FluidStack fluidStack = this.fluidStack.get();
        if (fluidStack.isEmpty()) {
            return Collections.singletonList(TransportText.SCREEN_FLUID_EMPTY);
        } else {
            return Lists.newArrayList(
                    TransportText.FLUID.withArgs(fluidStack.getDisplayName()
                            .deepCopy()
                            .mergeStyle(TextFormatting.WHITE)
                    ),
                    TransportText.AMOUNT.withArgs(
                            new StringTextComponent("" + fluidStack.getAmount())
                                    .mergeStyle(TextFormatting.WHITE),
                            new StringTextComponent("" + this.tankSize.getAsInt())
                                    .mergeStyle(TextFormatting.WHITE),
                            TransportText.MILLI_BUCKET
                    )
            );
        }
    }
}
