package xyz.brassgoggledcoders.transport.item;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

public class TransportCreativeModeTab extends CreativeModeTab {
    public TransportCreativeModeTab() {
        super(Transport.ID);
    }

    @Override
    @NotNull
    public ItemStack makeIcon() {
        return TransportBlocks.ONE_WAY_BOOSTER_RAIL.asStack();
    }

    @Override
    public void fillItemList(@NotNull NonNullList<ItemStack> pItems) {
        super.fillItemList(pItems);
        pItems.sort((o1, o2) -> {
            CompoundTag o1Tag = o1.getTagElement(ShellContentCreatorInfo.NBT_TAG_ELEMENT);
            CompoundTag o2Tag = o2.getTagElement(ShellContentCreatorInfo.NBT_TAG_ELEMENT);
            if (o1Tag != null && o2Tag == null) {
                return 1;
            } else if (o1Tag == null && o2Tag != null) {
                return -1;
            } else if (o1Tag != null) {
                return new ResourceLocation(o1Tag.getString("id")).compareTo(new ResourceLocation(o2Tag.getString("id")));
            } else {
                return 0;
            }
        });
    }
}
