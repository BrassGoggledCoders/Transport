package xyz.brassgoggledcoders.transport.api.container;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class NamedContainerProvider implements INamedContainerProvider {
    private final ITextComponent displayName;
    private final Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> containerProvider;

    public NamedContainerProvider(ITextComponent displayName, Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> containerProvider) {
        this.displayName = displayName;
        this.containerProvider = containerProvider;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return displayName;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return containerProvider.apply(id, playerInventory, playerEntity);
    }
}
