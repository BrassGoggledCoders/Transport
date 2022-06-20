package xyz.brassgoggledcoders.transport.api.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.items.IItemHandler;

public interface IRailProvider {
    Capability<IRailProvider> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    ItemStack findNext(IItemHandler searchable, boolean simulate);

    void nextPosition();
}
