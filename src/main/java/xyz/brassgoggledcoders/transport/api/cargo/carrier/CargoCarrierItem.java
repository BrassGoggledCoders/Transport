package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public class CargoCarrierItem implements ICargoCarrier {
    private final ItemStack itemStack;
    private Cargo cargo;
    private ICargoInstance cargoInstance;

    public CargoCarrierItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public Cargo getCargo() {
        if (cargo == null) {
            cargo = Optional.of(itemStack.getOrCreateChildTag("cargo"))
                    .map(nbtTagCompound -> nbtTagCompound.getString("name"))
                    .filter(string -> !string.isEmpty())
                    .map(ResourceLocation::new)
                    .map(TransportAPI.getCargoRegistry()::getEntry)
                    .orElseGet(TransportAPI.getCargoRegistry()::getEmpty);
        }
        return cargo;
    }

    @Override
    public ICargoInstance getCargoInstance() {
        if (cargoInstance == null) {
            cargoInstance = this.getCargo().create(this.getWorld().orElse(null));
        }
        return cargoInstance;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return false;
    }
}
