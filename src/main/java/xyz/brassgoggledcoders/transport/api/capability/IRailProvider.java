package xyz.brassgoggledcoders.transport.api.capability;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.Transport;

public interface IRailProvider {
    ItemCapability<IRailProvider, Void> CAPABILITY = ItemCapability.createVoid(Transport.rl("rail_provider"), IRailProvider.class);

    ItemStack findNext(IItemHandler searchable, boolean simulate);

    void nextPosition();
}
