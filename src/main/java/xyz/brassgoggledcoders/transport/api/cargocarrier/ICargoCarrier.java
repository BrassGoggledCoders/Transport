package xyz.brassgoggledcoders.transport.api.cargocarrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface ICargoCarrier {
    World getTheWorld();

    @Nonnull
    Cargo getCargo();

    @Nonnull
    CargoInstance getCargoInstance();

    void openContainer(PlayerEntity playerEntity, INamedContainerProvider provider, Consumer<PacketBuffer> packetBufferConsumer);

    boolean canInteractWith(PlayerEntity playerEntity);

    ITextComponent getCarrierDisplayName();
}
