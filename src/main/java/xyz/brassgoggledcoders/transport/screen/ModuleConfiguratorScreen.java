package xyz.brassgoggledcoders.transport.screen;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.capability.itemhandler.ModuleCaseItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ModuleConfiguratorScreen extends BasicAddonScreen {
    public ModuleConfiguratorScreen(BasicAddonContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    protected void func_230459_a_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
        if (this.minecraft != null && this.minecraft.player != null) {
            if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot instanceof SlotItemHandler &&
                    ((SlotItemHandler) this.hoveredSlot).getItemHandler() instanceof ModuleCaseItemStackHandler) {
                ModuleSlot moduleSlot = ((ModuleCaseItemStackHandler) ((SlotItemHandler) this.hoveredSlot).getItemHandler())
                        .getModuleSlot(this.hoveredSlot.getSlotIndex());
                if (this.hoveredSlot.getHasStack()) {
                    ItemStack hoveredStack = hoveredSlot.getStack();
                    net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(hoveredStack);
                    List<ITextComponent> tooltips = this.getTooltipFromItem(hoveredStack);
                    tooltips.add(0, new TranslationTextComponent("text.transport.module_slot", moduleSlot.getDisplayName()));
                    this.renderTooltip(matrixStack, tooltips, mouseX, mouseY);
                    net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
                } else {
                    this.renderTooltip(matrixStack, Lists.newArrayList(
                            new TranslationTextComponent("text.transport.module_slot", moduleSlot.getDisplayName())),
                            mouseX, mouseY);
                }
            } else {
                super.func_230459_a_(matrixStack, mouseX, mouseY);
            }
        } else {
            super.func_230459_a_(matrixStack, mouseX, mouseY);
        }
    }
}
