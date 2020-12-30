package xyz.brassgoggledcoders.transport.container.provider;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class EntityContainerProvider<T extends Entity> implements INamedContainerProvider {
    private final ITextComponent displayName;
    private final T entity;
    private final Function3<Integer, PlayerInventory, T, Container> containerCreator;

    public EntityContainerProvider(T entity, Function3<Integer, PlayerInventory, T, Container> containerCreator) {
        this.displayName = entity.getDisplayName();
        this.entity = entity;
        this.containerCreator = containerCreator;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return this.displayName;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {

        return containerCreator.apply(windowId, playerInventory, entity);
    }
}
