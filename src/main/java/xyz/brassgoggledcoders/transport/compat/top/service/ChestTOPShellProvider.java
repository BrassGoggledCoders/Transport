package xyz.brassgoggledcoders.transport.compat.top.service;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.api.IProbeConfig.ConfigMode;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import javax.annotation.Nonnull;
import java.util.*;

public class ChestTOPShellProvider implements ITOPShellProvider {

    @Override
    public void addInfo(ShellContent shellContent, ProbeMode mode, IProbeInfo probeInfo, IProbeConfig probeConfig) {
        List<ItemStack> stacks = null;
        ConfigMode chestMode = probeConfig.getShowChestContents();

        Block viewBlock = shellContent.getViewBlockState()
                .getBlock();

        ResourceLocation viewBlockId = ForgeRegistries.BLOCKS.getKey(viewBlock);

        boolean inventoryToShow = Config.getInventoriesToShow()
                .contains(viewBlockId);

        boolean inventoryToNotShow = Config.getInventoriesToNotShow()
                .contains(viewBlockId);

        int showSmallChestContents = Config.showSmallChestContentsWithoutSneaking.get();

        if (chestMode != ConfigMode.EXTENDED || showSmallChestContents <= 0 && Config.getInventoriesToShow().isEmpty()) {
            if (chestMode == ConfigMode.NORMAL && !Config.getInventoriesToNotShow().isEmpty() && inventoryToNotShow) {
                chestMode = ConfigMode.EXTENDED;
            }
        } else if (inventoryToShow) {
            chestMode = ConfigMode.NORMAL;
        } else if (showSmallChestContents > 0) {
            stacks = new ArrayList<>();
            int slots = getChestContents(shellContent, stacks);
            if (slots <= Config.showSmallChestContentsWithoutSneaking.get()) {
                chestMode = ConfigMode.NORMAL;
            }
        }

        if (Tools.show(mode, chestMode)) {
            if (stacks == null) {
                stacks = new ArrayList<>();
                getChestContents(shellContent, stacks);
            }

            if (!stacks.isEmpty()) {
                boolean showDetailed = Tools.show(mode, probeConfig.getShowChestContentsDetailed()) && stacks.size() <= Config.showItemDetailThresshold.get();
                showChestContents(probeInfo, stacks, showDetailed);
            }
        }
    }

    private static void showChestContents(IProbeInfo probeInfo, List<ItemStack> stacks, boolean detailed) {
        int rows = 0;
        int idx = 0;
        IProbeInfo horizontal = null;
        IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0));
        Iterator<ItemStack> var9;
        ItemStack stackInSlot;
        if (detailed) {
            var9 = stacks.iterator();

            while (var9.hasNext()) {
                stackInSlot = var9.next();
                horizontal = vertical.horizontal((new LayoutStyle()).spacing(10).alignment(ElementAlignment.ALIGN_CENTER));
                horizontal.item(stackInSlot, (new ItemStyle()).width(16).height(16)).text(CompoundText.create().info(stackInSlot.getDescriptionId()));
            }
        } else {
            for (var9 = stacks.iterator(); var9.hasNext(); ++idx) {
                stackInSlot = var9.next();
                if (idx % 10 == 0) {
                    horizontal = vertical.horizontal((new LayoutStyle()).spacing(0));
                    ++rows;
                    if (rows > 4) {
                        break;
                    }
                }

                horizontal.item(stackInSlot);
            }
        }

    }

    private static int getChestContents(ShellContent shellContent, List<ItemStack> stacks) {
        Set<Item> foundItems = Config.compactEqualStacks.get() ? new HashSet<>() : null;

        return shellContent.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .map((capability) -> {
                    for (int i = 0; i < capability.getSlots(); ++i) {
                        addItemStack(stacks, foundItems, capability.getStackInSlot(i));
                    }

                    return capability.getSlots();
                })
                .orElse(0);
    }

    private static void addItemStack(List<ItemStack> stacks, Set<Item> foundItems, @Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            if (foundItems != null && foundItems.contains(stack.getItem())) {

                for (ItemStack s : stacks) {
                    if (ItemHandlerHelper.canItemStacksStack(s, stack)) {
                        s.grow(stack.getCount());
                        return;
                    }
                }
            }

            stacks.add(stack.copy());
            if (foundItems != null) {
                foundItems.add(stack.getItem());
            }
        }
    }
}
