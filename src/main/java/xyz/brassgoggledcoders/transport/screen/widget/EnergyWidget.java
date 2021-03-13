package xyz.brassgoggledcoders.transport.screen.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import xyz.brassgoggledcoders.transport.content.TransportText;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.IntSupplier;

public class EnergyWidget extends BasicWidget implements IHoverableWidget {
    private final static ResourceLocation COMPONENTS = new ResourceLocation("titanium", "textures/gui/background.png");

    private final IntSupplier energy;
    private final IntSupplier maxEnergy;

    public EnergyWidget(int x, int y, IntSupplier energy, IntSupplier maxEnergy) {
        super(x, y, 18, 56);
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

            int powerOffset = (int) (energy.getAsInt() / Math.max(maxEnergy.getAsInt(), 1.0D) * (double) 50);
            this.blit(matrixStack, x + 3, y + 53 - powerOffset, 196, 97, width - 6, powerOffset);
        }
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    @Nonnull
    public List<? extends ITextProperties> getHoveredText() {
        return Lists.newArrayList(
                TransportText.AMOUNT.withArgs(
                        new StringTextComponent("" + energy.getAsInt())
                                .mergeStyle(TextFormatting.WHITE),
                        new StringTextComponent("" + maxEnergy.getAsInt())
                                .mergeStyle(TextFormatting.WHITE),
                        TransportText.FORGE_ENERGY
                )
        );
    }
}
