package xyz.brassgoggledcoders.transport.capability;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;

import java.util.List;

public class InventoryPlusComponent<T extends IComponentHarness> extends InventoryComponent<T> implements IContainerAddon {
    public InventoryPlusComponent(String name, int xPos, int yPos, int size) {
        super(name, xPos, yPos, size);
    }

    @Override
    public List<Slot> getSlots(Container container) {
        List<Slot> slots = Lists.newArrayList();
        int i = 0;
        for (int y = 0; y < this.getYSize(); ++y) {
            for (int x = 0; x < this.getXSize(); ++x) {
                slots.add(new SlotItemHandler(this, i, this.getXPos() +
                        this.getSlotPosition().apply(i).getLeft(), this.getYPos() +
                        this.getSlotPosition().apply(i).getRight()));
                ++i;
            }
        }
        return slots;
    }
}
