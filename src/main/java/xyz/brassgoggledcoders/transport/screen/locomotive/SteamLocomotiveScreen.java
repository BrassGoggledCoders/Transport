package xyz.brassgoggledcoders.transport.screen.locomotive;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.engine.EngineState;
import xyz.brassgoggledcoders.transport.container.locomotive.SteamLocomotiveContainer;
import xyz.brassgoggledcoders.transport.content.TransportFluids;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.engine.SteamEngine;
import xyz.brassgoggledcoders.transport.screen.util.FluidRenderer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class SteamLocomotiveScreen extends ContainerScreen<SteamLocomotiveContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/steam_locomotive.png");

    private SpeedWidget speedWidget;

    public SteamLocomotiveScreen(SteamLocomotiveContainer screenContainer, PlayerInventory playerInventory,
                                 ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new PowerButton(guiLeft + 73, guiTop + 36, 12, 12, this.getContainer().getOn(),
                this.getContainer().getPropertyManager()));
        this.addButton(speedWidget = new SpeedWidget(this, guiLeft + 91, guiTop + 17, 77, 43));
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

        if (!this.getContainer().isLocomotiveOn()) {
            this.blit(matrixStack, i + 73, j + 36, 210, 30, 12, 12);
        }

        int burnedAmount = this.getContainer().getFuelBurnedScaled();
        if (burnedAmount > 0) {
            this.blit(matrixStack, i + 76, j + 55, 184, 15, 7, burnedAmount);
        }

        FluidRenderer.renderFluid(matrixStack, this.getContainer().getWater(), SteamEngine.WATER_CAPACITY,
                i + 30, j + 19, 20, 49);
        FluidRenderer.renderFluid(matrixStack, new FluidStack(TransportFluids.STEAM.get(), this.getContainer().getSteam()),
                4000, i + 55, j + 21, 7, 47);
        this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
        this.blit(matrixStack, i + 30, j + 19, 177, 31, 20, 51);

        RenderSystem.popMatrix();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        if (this.speedWidget.isHovered()) {
            EngineState engineState = speedWidget.getEngineState();
            if (engineState != null) {
                GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(
                        new TranslationTextComponent("screen.transport.speed", engineState.getDisplayName())),
                        mouseX, mouseY, width, height, -1, Minecraft.getInstance().fontRenderer);
            }
        }
        if (isIn(mouseX, mouseY, 30, 19, 20, 49)) {
            FluidStack waterStack = this.getContainer().getWater();
            List<ITextComponent> tooltip;
            if (!waterStack.isEmpty()) {
                tooltip = Lists.newArrayList(
                        waterStack.getDisplayName(),
                        new TranslationTextComponent("screen.transport.fluid.capacity",
                                waterStack.getAmount(), SteamEngine.WATER_CAPACITY)
                );
            } else {
                tooltip = Lists.newArrayList(TransportText.SCREEN_FLUID_EMPTY);
            }
            GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, width, height, -1,
                    Minecraft.getInstance().fontRenderer);

        }
    }

    private boolean isIn(int mouseX, int mouseY, int xOffset, int yOffset, int width, int height) {
        return mouseX > xOffset + guiLeft && mouseX < xOffset + guiLeft + width &&
                mouseY > yOffset + guiTop && mouseY < yOffset + guiTop + height;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!dragSplitting) {
            for (IGuiEventListener eventListener : this.getEventListeners()) {
                if (eventListener.isMouseOver(mouseX, mouseY) &&
                        eventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                    return true;
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
