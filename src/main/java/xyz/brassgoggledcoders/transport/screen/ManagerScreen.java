package xyz.brassgoggledcoders.transport.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.container.ManagerContainer;
import xyz.brassgoggledcoders.transport.api.manager.ManagedObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class ManagerScreen extends ContainerScreen<ManagerContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = Transport.rl("textures/screen/manager.png");
    private float sliderProgress;
    private boolean clickedOnScroll;
    private int recipeIndexOffset;

    public ManagerScreen(ManagerContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        --this.titleY;
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        int k = (int) (41.0F * this.sliderProgress);
        this.blit(matrixStack, i + 75, j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        int l = this.guiLeft + 8;
        int i1 = this.guiTop + 14;
        int j1 = this.recipeIndexOffset + 12;
        this.func_238853_b_(matrixStack, x, y, l, i1, j1);
        this.drawRecipesItems(l, i1, j1);
    }

    @Override
    protected void renderHoveredTooltip(@Nonnull MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
        int i = this.guiLeft + 8;
        int j = this.guiTop + 14;
        int k = this.recipeIndexOffset + 12;
        List<ManagedObject> list = this.container.getManagedObjects();

        for (int l = this.recipeIndexOffset; l < k && l < this.container.getManagedObjects().size(); ++l) {
            int i1 = l - this.recipeIndexOffset;
            int j1 = i + i1 % 4 * 16;
            int k1 = j + i1 / 4 * 18 + 2;
            if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
                this.renderTooltip(matrixStack, list.get(l).getRepresentative(), x, y);
            }
        }
    }

    private void func_238853_b_(MatrixStack p_238853_1_, int p_238853_2_, int p_238853_3_, int p_238853_4_, int p_238853_5_, int p_238853_6_) {
        for (int i = this.recipeIndexOffset; i < p_238853_6_ && i < this.container.getManagedObjects().size(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = p_238853_4_ + j % 4 * 16;
            int l = j / 4;
            int i1 = p_238853_5_ + l * 18 + 2;
            int j1 = this.ySize;
            if (i == this.container.getSelectedObject().get()) {
                j1 += 18;
            } else if (p_238853_2_ >= k && p_238853_3_ >= i1 && p_238853_2_ < k + 16 && p_238853_3_ < i1 + 18) {
                j1 += 36;
            }

            this.blit(p_238853_1_, k, i1 - 1, 0, j1, 16, 18);
        }

    }

    private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
        List<ManagedObject> list = this.container.getManagedObjects();

        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getManagedObjects().size(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 16;
            int l = j / 4;
            int i1 = top + l * 18 + 2;
            this.getMinecraft().getItemRenderer().renderItemAndEffectIntoGUI(list.get(i).getRepresentative(), k, i1);
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_231044_5_) {
        this.clickedOnScroll = false;
        int i = this.guiLeft + 8;
        int j = this.guiTop + 14;
        int k = this.recipeIndexOffset + 12;

        for (int l = this.recipeIndexOffset; l < k; ++l) {
            int i1 = l - this.recipeIndexOffset;
            double d0 = mouseX - (double) (i + i1 % 4 * 16);
            double d1 = mouseY - (double) (j + i1 / 4 * 18);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.container.enchantItem(
                    Objects.requireNonNull(this.getMinecraft().player), l)) {
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                Objects.requireNonNull(this.getMinecraft().playerController).sendEnchantPacket((this.container).windowId, l);
                return true;
            }
        }

        i = this.guiLeft + 75;
        j = this.guiTop + 9;
        if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
            this.clickedOnScroll = true;
        }

        return super.mouseClicked(mouseX, mouseY, p_231044_5_);
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        if (this.clickedOnScroll && this.canScroll()) {
            int i = this.guiTop + 14;
            int j = i + 54;
            this.sliderProgress = ((float) p_231045_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double p_231043_5_) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float) ((double) this.sliderProgress - p_231043_5_ / (double) i);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
        }

        return true;
    }

    private boolean canScroll() {
        return this.container.getManagedObjects().size() > 12;
    }

    protected int getHiddenRows() {
        return (this.container.getManagedObjects().size() + 4 - 1) / 4 - 3;
    }
}