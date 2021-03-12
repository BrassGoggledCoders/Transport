package xyz.brassgoggledcoders.transport.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.transport.screen.util.FluidRenderer;

import javax.annotation.Nonnull;
import java.util.function.IntSupplier;

public class EnergyWidget extends Widget {
    private final static ResourceLocation COMPONENTS = new ResourceLocation("titanium", "textures/gui/background.png");

    private final IntSupplier energy;
    private final IntSupplier maxEnergy;

    public EnergyWidget(int x, int y, IntSupplier energy, IntSupplier maxEnergy) {
        super(x, y, 18, 56, new StringTextComponent("Energy Storage"));
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (this.visible) {
            Minecraft.getInstance()
                    .getTextureManager()
                    .bindTexture(COMPONENTS);
            this.blit(matrixStack, x, y, 177, 94, width, height);

            int powerOffset = (int)(energy.getAsInt() / Math.max(maxEnergy.getAsInt(), 1.0D) * (double)50);
            this.blit(matrixStack, x + 3, y + 53 - powerOffset, 196, 97, width - 6, powerOffset);
        }
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }
}
