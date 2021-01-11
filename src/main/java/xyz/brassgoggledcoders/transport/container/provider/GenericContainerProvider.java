package xyz.brassgoggledcoders.transport.container.provider;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class GenericContainerProvider implements INamedContainerProvider {
    private final NonNullLazy<ITextComponent> displayName;
    private final Function3<Integer, PlayerInventory, PlayerEntity, Container> containerCreator;

    public GenericContainerProvider(NonNullSupplier<ITextComponent> displayName, Function3<Integer, PlayerInventory, PlayerEntity,
            Container> containerCreator) {
        this.displayName = NonNullLazy.of(displayName);
        this.containerCreator = containerCreator;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return displayName.get();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return containerCreator.apply(windowId, playerInventory, playerEntity);
    }
}
