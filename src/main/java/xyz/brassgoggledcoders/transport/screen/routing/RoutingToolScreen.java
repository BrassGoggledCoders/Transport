package xyz.brassgoggledcoders.transport.screen.routing;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.routing.RoutingToolContainer;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingEdge;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNode;
import xyz.brassgoggledcoders.transport.routingnetwork.RoutingNodeType;
import xyz.brassgoggledcoders.transport.screen.BasicContainerScreen;
import xyz.brassgoggledcoders.transport.screen.util.ScreenHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class RoutingToolScreen extends BasicContainerScreen<RoutingToolContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/routing_tool.png");

    private final RoutingChoiceButton[] routingChoiceButtons = new RoutingChoiceButton[7];

    private boolean clickedToDrag;
    private int offset;

    public RoutingToolScreen(RoutingToolContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
        this.xSize = 276;
        this.playerInventoryTitleX = 107;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int k = j + 16 + 2;

        for (int l = 0; l < 7; ++l) {
            this.routingChoiceButtons[l] = this.addButton(new RoutingChoiceButton(this.container, this::getOffset, l, i + 5, k));
            k += 20;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void midRender(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        List<Pair<RoutingNode, RoutingEdge>> neighbors = this.container.getNeighbors();
        if (!neighbors.isEmpty()) {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            RenderSystem.pushMatrix();
            RenderSystem.enableRescaleNormal();
            this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
            this.drawScrollBar(matrixStack, i, j, neighbors);
            int i1 = 0;

            for (Pair<RoutingNode, RoutingEdge> neighbor : neighbors) {
                if (!this.checkOverSized(neighbors.size()) || (i1 >= this.offset && i1 < 7 + this.offset)) {
                    ItemStack itemstack = neighbor.getKey().getType() == RoutingNodeType.STATION ?
                            TransportBlocks.DOCK.asStack() : TransportBlocks.BUOY.asStack();
                    this.itemRenderer.zLevel = 100.0F;
                    int j1 = k + 2;
                    this.renderButtonStacks(matrixStack, itemstack, l, j1);
                    this.itemRenderer.zLevel = 0.0F;
                    k += 20;
                }
                ++i1;
            }

            for (RoutingChoiceButton routingChoiceButton : this.routingChoiceButtons) {
                if (routingChoiceButton.isHovered()) {
                    routingChoiceButton.renderToolTip(matrixStack, mouseX, mouseY);
                }

                routingChoiceButton.visible = routingChoiceButton.getIndex() < this.container.getNeighbors().size();
            }

            RenderSystem.popMatrix();
            RenderSystem.enableDepthTest();
        }

    }

    private void renderButtonStacks(MatrixStack matrixStack, ItemStack itemStack, int x, int y) {
        this.itemRenderer.renderItemOverlayIntoGUI(this.font, itemStack, x, y, null);
        this.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
        this.setBlitOffset(this.getBlitOffset() + 300);
        blit(matrixStack, x + 7, y + 12, this.getBlitOffset(), 0.0F, 176.0F, 9, 2, 256, 512);
        this.setBlitOffset(this.getBlitOffset() - 300);
    }

    private void drawScrollBar(MatrixStack matrixStack, int middleX, int middleY, List<Pair<RoutingNode, RoutingEdge>> neighbors) {
        int i = neighbors.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int i1 = Math.min(113, this.offset * k);
            if (this.offset == i - 1) {
                i1 = 113;
            }

            blit(matrixStack, middleX + 94, middleY + 18 + i1, this.getBlitOffset(), 0.0F, 199.0F, 6, 27, 256, 512);
        } else {
            blit(matrixStack, middleX + 94, middleY + 18, this.getBlitOffset(), 6.0F, 199.0F, 6, 27, 256, 512);
        }

    }

    private int getOffset() {
        return offset;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        ScreenHelper.renderBackground(this, BACKGROUND, matrixStack);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = this.container.getNeighbors().size();
        if (this.checkOverSized(i)) {
            int j = i - 7;
            this.offset = (int) ((double) this.offset - delta);
            this.offset = MathHelper.clamp(this.offset, 0, j);
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int i = this.container.getNeighbors().size();
        if (this.clickedToDrag) {
            int j = this.guiTop + 18;
            int k = j + 139;
            int l = i - 7;
            float f = ((float) mouseY - (float) j - 13.5F) / ((float) (k - j) - 27.0F);
            f = f * (float) l + 0.5F;
            this.offset = MathHelper.clamp((int) f, 0, l);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clickedToDrag = false;
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        if (this.checkOverSized(this.container.getNeighbors().size()) &&
                mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) &&
                mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
            this.clickedToDrag = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean checkOverSized(int size) {
        return size > 7;
    }
}
