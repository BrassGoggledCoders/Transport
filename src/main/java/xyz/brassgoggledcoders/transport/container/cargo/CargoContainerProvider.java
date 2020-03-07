package xyz.brassgoggledcoders.transport.container.cargo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.brassgoggledcoders.transport.api.cargocarrier.ICargoCarrier;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class CargoContainerProvider implements INamedContainerProvider {
    private final CargoInstance cargoInstance;
    private final ICargoCarrier cargoCarrier;

    public CargoContainerProvider(ICargoCarrier carrier, CargoInstance cargoInstance) {
        this.cargoInstance = cargoInstance;
        this.cargoCarrier = carrier;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("text.transport.with", cargoCarrier.getCarrierDisplayName(),
                cargoInstance.getDisplayName())
                .applyTextStyles(TextFormatting.BLACK);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CargoContainer(containerId, cargoInstance, playerInventory);
    }
}
