package xyz.brassgoggledcoders.transport.service;

import com.mojang.datafixers.util.Function3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.service.IItemHelperService;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContentCreatorInfo;
import xyz.brassgoggledcoders.transport.content.TransportText;
import xyz.brassgoggledcoders.transport.item.DispenserMinecartItemBehavior;
import xyz.brassgoggledcoders.transport.item.ShellMinecartItem;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemHelperServiceImpl implements IItemHelperService {
    @Override
    public <T extends AbstractMinecart> DispenseItemBehavior createMinecartDispenseBehavior(
            Function3<ItemStack, Level, Vec3, T> minecartCreator
    ) {
        return new DispenserMinecartItemBehavior(minecartCreator);
    }

    @Override
    public <T extends AbstractMinecart & IShell> Function<Item.Properties, Item> createShellMinecartItem(
            Function3<ItemStack, Level, Vec3, T> minecartCreator
    ) {
        return properties -> {
            Item item = new ShellMinecartItem<>(properties, minecartCreator);
            DispenserBlock.registerBehavior(
                    item,
                    this.createMinecartDispenseBehavior(minecartCreator)
            );
            return item;
        };
    }

    @Override
    public void appendTextForShellItems(ItemStack pStack, Consumer<Component> consumer) {
        ShellContentCreatorInfo info = Optional.ofNullable(pStack.getTagElement(ShellContentCreatorInfo.NBT_TAG_ELEMENT))
                .map(nbt -> ResourceLocation.tryParse(nbt.getString(ShellContentCreatorInfo.NBT_TAG_ID)))
                .map(TransportAPI.SHELL_CONTENT_CREATOR.get()::getById)
                .orElseGet(TransportAPI.SHELL_CONTENT_CREATOR.get()::getEmpty);

        consumer.accept(
                //TODO: Test Translation?
                Component.translatable(TransportText.SHELL_CONTENT_COMPONENT.getString(), info.name())
                        .withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public ItemStack appendShellNBT(ItemStack pStack, ShellContent shellContent, boolean includeData) {
        CompoundTag compoundTag = pStack.getOrCreateTagElement(ShellContentCreatorInfo.NBT_TAG_ELEMENT);
        compoundTag.putString(ShellContentCreatorInfo.NBT_TAG_ID, shellContent.getCreatorInfo().id().toString());
        if (includeData) {
            compoundTag.put(ShellContentCreatorInfo.NBT_TAG_DATA, shellContent.serializeNBT());
        }

        return pStack;
    }
}
