package xyz.brassgoggledcoders.transport.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiContainerEvent.DrawBackground;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.ModuleTab;
import xyz.brassgoggledcoders.transport.screen.ModularScreenInfo;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = Transport.ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ClientForgeEventHandler {
    private static final ResourceLocation TABS = Transport.rl("textures/screen/components.png");

    @SubscribeEvent
    public static void onModuleTabClick(MouseClickedEvent mouseClickedEvent) {
        Screen screen = mouseClickedEvent.getGui();
        if (mouseClickedEvent.getButton() == 0 && screen instanceof ContainerScreen<?>) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>) screen;
            Container container = containerScreen.getContainer();
            ModularScreenInfo modularScreenInfo = ModularScreenInfo.getCurrent();
            if (modularScreenInfo.matches(container)) {
                int screenLeft = containerScreen.getGuiLeft();
                double mouseX = mouseClickedEvent.getMouseX();
                if (mouseX < screenLeft && mouseX > screenLeft - 32) {
                    int screenTop = containerScreen.getGuiTop();
                    double mouseY = mouseClickedEvent.getMouseY();
                    if (mouseY > screenTop) {
                        double difference = mouseY - screenTop;
                        int tab = Math.floorDiv((int) Math.floor(difference), 28);
                        if (tab >= 0 && tab < modularScreenInfo.getModuleTabList().size()) {
                            ModuleTab moduleTab = modularScreenInfo.getModuleTabList().get(tab);
                            if (!moduleTab.getUniqueId().equals(modularScreenInfo.getPicked())) {
                                Transport.instance.networkHandler.sendTabClickedMessage(
                                        modularScreenInfo.getEntityId(),
                                        modularScreenInfo.getModuleTabList()
                                                .get(tab)
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderModuleTabs(DrawBackground drawScreenEvent) {
        ContainerScreen<?> screen = drawScreenEvent.getGuiContainer();
        Container container = ((IHasContainer<?>) screen).getContainer();
        ModularScreenInfo modularScreenInfo = ModularScreenInfo.getCurrent();
        if (modularScreenInfo.matches(container)) {
            loopTabs(screen,
                    drawScreenEvent.getMatrixStack(),
                    modularScreenInfo.getModuleTabList(),
                    modularScreenInfo.getPicked()
            );
        }
    }

    private static void loopTabs(ContainerScreen<?> screen, MatrixStack matrixStack, List<ModuleTab> tabs, UUID current) {
        if (!tabs.isEmpty()) {
            int i = 0;
            Iterator<ModuleTab> iterator = tabs.iterator();
            while (i < 5 && iterator.hasNext()) {
                ModuleTab moduleTab = iterator.next();
                renderTab(screen, moduleTab, matrixStack, moduleTab.getUniqueId().equals(current), i,
                        screen.getGuiLeft() - 28, screen.getGuiTop() + (i * 28));
                i++;
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void renderTab(ContainerScreen<?> screen, ModuleTab moduleTab, MatrixStack matrixStack, boolean selected, int tab, int x, int y) {
        RenderSystem.color3f(1F, 1F, 1F);
        RenderSystem.enableBlend();
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(TABS);
        screen.blit(matrixStack, x, y, selected ? 32 : 0, tab * 28, 32, 28);
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        itemRenderer.zLevel = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemstack = moduleTab.getDisplayStack();
        itemRenderer.renderItemAndEffectIntoGUI(itemstack, x + 9, y + 6);
        itemRenderer.renderItemOverlays(minecraft.fontRenderer, itemstack, x + 9, y + 6);
        itemRenderer.zLevel = 0.0F;
    }
}
