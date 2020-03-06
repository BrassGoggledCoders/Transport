package xyz.brassgoggledcoders.transport.container.containeraddon;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;

import java.util.Collections;
import java.util.List;

public interface IContainerAddon {
    default List<Slot> getSlots(Container container) {
        return Collections.emptyList();
    }

    default List<IntReferenceHolder> getTrackedInts() {
        return Collections.emptyList();
    }

    default List<IIntArray> getTrackedIntArrays() {
        return Collections.emptyList();
    }
}
