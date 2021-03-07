package xyz.brassgoggledcoders.transport.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiContainerEvent.DrawBackground;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;
import xyz.brassgoggledcoders.transport.screen.ModularScreenInfo;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = Transport.ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ClientForgeEventHandler {
    private static final ResourceLocation TABS = Transport.rl("textures/screen/components.png");

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
