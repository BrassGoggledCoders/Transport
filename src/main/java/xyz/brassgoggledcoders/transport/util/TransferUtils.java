package xyz.brassgoggledcoders.transport.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.BiConsumer;

public class TransferUtils {
    public static void transferEnergy(IEnergyStorage from, IEnergyStorage to) {
        int amountSimPulled = from.extractEnergy(5000, true);
        if (amountSimPulled > 0) {
            int amountSimPushed = to.receiveEnergy(amountSimPulled, true);
            if (amountSimPushed > 0) {
                to.receiveEnergy(from.extractEnergy(amountSimPushed, false), false);
            }
        }
    }

    public static void transferInventory(IItemHandler from, IItemHandler to) {
        int moved = 0;
        int slotNumber = 0;
        do {
            ItemStack itemStack = from.extractItem(slotNumber, 16, true);
            if (!itemStack.isEmpty()) {
                if (ItemHandlerHelper.insertItem(to, itemStack, true).isEmpty()) {
                    ItemStack movedItemStack = from.extractItem(slotNumber, 16, false);
                    moved += movedItemStack.getCount();
                    ItemHandlerHelper.insertItem(to, movedItemStack, false);
                }
            }
            slotNumber++;
        } while (slotNumber < from.getSlots() && moved < 32);
    }

    public static void transferFluid(IFluidHandler from, IFluidHandler to) {
        FluidStack output = from.drain(5000, IFluidHandler.FluidAction.SIMULATE);
        if (!output.isEmpty()) {
            int filledAmount = to.fill(output, IFluidHandler.FluidAction.SIMULATE);
            if (filledAmount > 0) {
                to.fill(from.drain(filledAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public static <CAP> void tryTransfer(BiConsumer<CAP, CAP> transfer, LazyOptional<CAP> lazyFrom, LazyOptional<CAP> lazyTo) {
        lazyFrom.ifPresent(from -> lazyTo.ifPresent(to -> transfer.accept(from, to)));
    }
}
