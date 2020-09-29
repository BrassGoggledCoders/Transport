package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.item.ItemStack;

public interface IBookHolder {
    ItemStack getBook();

    int getOpenPage();
}
