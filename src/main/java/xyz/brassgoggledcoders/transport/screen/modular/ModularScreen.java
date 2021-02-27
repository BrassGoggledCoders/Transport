package xyz.brassgoggledcoders.transport.screen.modular;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.modular.ModularContainer;
import xyz.brassgoggledcoders.transport.network.property.Property;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class ModularScreen extends ContainerScreen<ModularContainer> implements IModularScreen {
    private static final ResourceLocation TABS = Transport.rl("textures/screen/components.png");
    private ModuleScreen<?> activeScreen;
    private ModuleContainer lastActiveContainer;

    public ModularScreen(ModularContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        List<ModuleTab<?>> moduleTabList = this.getContainer().getExistingModuleTabs();
        ModuleTab<?> activeTab = this.getContainer().getActiveTab();
        loopTabs(matrixStack, moduleTabList, activeTab, false);
        ResourceLocation background = this.getActiveScreen().getBackgroundTexture();
        if (background != null) {
            this.getMinecraft().getTextureManager().bindTexture(background);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        }
        this.getActiveScreen().drawBackgroundLayer(matrixStack, partialTicks, x, y);
        loopTabs(matrixStack, moduleTabList, activeTab, true);
    }

    private void loopTabs(MatrixStack matrixStack, List<ModuleTab<?>> tabs, ModuleTab<?> current, boolean selected) {
        if (!tabs.isEmpty()) {
            int i = 0;
            Iterator<ModuleTab<?>> iterator = tabs.iterator();
            while (i < 5 && iterator.hasNext()) {
                ModuleTab<?> moduleTab = iterator.next();
                if ((moduleTab == current) == selected) {
                    renderTab(moduleTab, matrixStack, selected, i, this.guiLeft - 28, this.guiTop + (i * 28));
                }
                i++;
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void renderTab(ModuleTab<?> moduleTab, MatrixStack matrixStack, boolean selected, int tab, int x, int y) {
        RenderSystem.color3f(1F, 1F, 1F);
        RenderSystem.enableBlend();
        this.getMinecraft().getTextureManager().bindTexture(TABS);
        this.blit(matrixStack, x, y, selected ? 32 : 0, tab * 28, 32, 28);
        this.itemRenderer.zLevel = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemstack = moduleTab.getDisplayStack();
        this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, x + 9, y + 6);
        this.itemRenderer.renderItemOverlays(this.font, itemstack, x + 9, y + 6);
        this.itemRenderer.zLevel = 0.0F;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.getActiveScreen().drawForegroundLayer(matrixStack, x, y);
    }

    @Override
    protected void init() {
        this.getActiveScreen().init();
        super.init();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.push();
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.getActiveScreen().render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        matrixStack.pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (mouseX < this.getLeft() && mouseX > this.getLeft() - 32) {
            int down = (int) (mouseY - this.getTop());
            int tab = down / 28;
            if (tab < this.getContainer().getExistingModuleTabs().size()) {
                Property<Integer> activeTabIndex = this.getContainer()
                        .getActiveTabIndexProperty();
                this.getContainer()
                        .getPropertyManager()
                        .updateServer(activeTabIndex, tab);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void init(@Nonnull Minecraft minecraft, int width, int height) {
        this.getActiveScreen().init(minecraft, width, height);
        super.init(minecraft, width, height);
    }

    private ModuleScreen<?> getActiveScreen() {
        ModularContainer container = this.getContainer();
        if (this.lastActiveContainer != container.getActiveContainer()) {
            this.lastActiveContainer = container.getActiveContainer();
            ModuleScreen<?> moduleScreen = container.getActiveTab()
                    .getModuleScreenCreator()
                    .get()
                    .apply(this, container.getActiveContainer());
            this.xSize = moduleScreen.getXSize();
            this.ySize = moduleScreen.getYSize();
            if (this.minecraft != null) {
                moduleScreen.init(this.getMinecraft(), this.width, this.height);
                moduleScreen.init();
                super.init();
            }

            this.activeScreen = moduleScreen;
        }
        return this.activeScreen;
    }

    @Override
    public int getLeft() {
        return this.getGuiLeft();
    }

    @Override
    public int getTop() {
        return this.getGuiTop();
    }
}
