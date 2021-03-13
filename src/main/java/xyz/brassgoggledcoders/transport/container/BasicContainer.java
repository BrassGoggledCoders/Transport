package xyz.brassgoggledcoders.transport.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.IWorldPosCallable;
import xyz.brassgoggledcoders.transport.network.property.IPropertyManaged;
import xyz.brassgoggledcoders.transport.network.property.PropertyManager;
import xyz.brassgoggledcoders.transport.util.WorldHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public class BasicContainer extends Container implements IPropertyManaged {
    private final IWorldPosCallable worldPosCallable;
    private final PropertyManager propertyManager;

    protected BasicContainer(@Nullable ContainerType<?> type, int id, IWorldPosCallable worldPosCallable) {
        super(type, id);
        this.propertyManager = new PropertyManager((short) id);
        this.worldPosCallable = worldPosCallable;
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return this.worldPosCallable.applyOrElse(WorldHelper.isPlayerNear(player)::test, true);
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        this.getPropertyManager().sendChanges(Collections.singletonList(listener), true);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        this.getPropertyManager().sendChanges(this.listeners,false);
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }
}
