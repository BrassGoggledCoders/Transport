package xyz.brassgoggledcoders.transport.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IntReferenceHolder;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.api.manager.ManagedObject;

import javax.annotation.Nonnull;
import java.util.List;

public class ManagerContainer extends Container {
    private final List<ManagedObject> managedObjects;
    private final IntReferenceHolder selectedObject = IntReferenceHolder.single();

    public ManagerContainer(int id, PlayerInventory inventory, List<ManagedObject> managedObjects) {
        super(TransportContainers.MANAGER.get(), id);
        this.managedObjects = managedObjects;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    public List<ManagedObject> getManagedObjects() {
        return this.managedObjects;
    }

    public IntReferenceHolder getSelectedObject() {
        return selectedObject;
    }

    @Override
    public boolean enchantItem(@Nonnull PlayerEntity player, int id) {
        if (this.checkValidIndex(id)) {
            this.selectedObject.set(id);
        }

        return true;
    }

    private boolean checkValidIndex(int index) {
        return index >= 0 && index < this.managedObjects.size();
    }
}
