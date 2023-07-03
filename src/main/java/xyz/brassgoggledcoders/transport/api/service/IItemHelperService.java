package xyz.brassgoggledcoders.transport.api.service;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xyz.brassgoggledcoders.transport.api.shell.IShell;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IItemHelperService {
    <T extends AbstractMinecart> DispenseItemBehavior createMinecartDispenseBehavior(
            Function3<ItemStack, Level, Vec3, T> minecartCreator
    );

    <T extends AbstractMinecart & IShell> Function<Item.Properties, Item> createShellMinecartItem(
            Function3<ItemStack, Level, Vec3, T> minecartCreator
    );

    void appendTextForShellItems(ItemStack pStack, Consumer<Component> consumer);

    ItemStack appendShellNBT(ItemStack pStack, ShellContent shellContent, boolean includeData);
}
