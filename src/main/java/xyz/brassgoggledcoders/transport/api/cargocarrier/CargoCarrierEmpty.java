package xyz.brassgoggledcoders.transport.api.cargocarrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.api.cargoinstance.EmptyCargoInstance;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

public class CargoCarrierEmpty implements ICargoCarrier {
    private final EmptyCargoInstance emptyCargoInstance;

    public CargoCarrierEmpty() {
        this.emptyCargoInstance = new EmptyCargoInstance();
    }

    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    @Nonnull
    public Cargo getCargo() {
        return TransportAPI.EMPTY_CARGO.get();
    }

    @Override
    @Nonnull
    public CargoInstance getCargoInstance() {
        return emptyCargoInstance;
    }

    @Override
    public void openContainer(PlayerEntity playerEntity, INamedContainerProvider provider, Consumer<PacketBuffer> packetBufferConsumer) {

    }

    @Override
    public boolean canInteractWith(PlayerEntity entityPlayer) {
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
