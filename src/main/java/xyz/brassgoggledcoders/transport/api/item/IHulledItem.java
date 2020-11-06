package xyz.brassgoggledcoders.transport.api.item;

import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

import javax.annotation.Nonnull;

public interface IHulledItem {
    @Nonnull
    HullType getHullType(@Nonnull ItemStack itemStack);
}
