package xyz.brassgoggledcoders.transport.container.cargo;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ScreenAddonCargoContainerProvider<T extends CargoInstance & IScreenAddonProvider> implements INamedContainerProvider {
    private final int entityId;
    private final CargoInstance cargoInstance;

    public ScreenAddonCargoContainerProvider(int entityId, CargoInstance cargoInstance) {
        this.entityId = entityId;
        this.cargoInstance = cargoInstance;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return cargoInstance.getDisplayName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return null;
    }
}
