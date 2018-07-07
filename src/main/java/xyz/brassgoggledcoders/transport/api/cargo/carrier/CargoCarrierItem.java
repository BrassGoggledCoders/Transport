package xyz.brassgoggledcoders.transport.api.cargo.carrier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.ICargo;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import java.util.Optional;

public class CargoCarrierItem implements ICargoCarrier {
    private final ItemStack itemStack;
    private ICargo cargo;

    public CargoCarrierItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Optional<World> getWorld() {
        return Optional.empty();
    }

    @Override
    public ICargo getCargo() {
        if (cargo == null) {
            cargo = Optional.ofNullable(itemStack.getTagCompound())
                    .map(nbtTagCompound -> nbtTagCompound.getCompoundTag("cargo"))
                    .map(nbtTagCompound -> nbtTagCompound.getString("name"))
                    .map(ResourceLocation::new)
                    .map(TransportAPI.getCargoRegistry()::getEntry)
                    .orElseGet(TransportAPI.getCargoRegistry()::getEmpty);
        }
        return cargo;
    }

    @Override
    public ICargoInstance getCargoInstance() {
        return null;
    }

    @Override
    public float getBrightness() {
        return 0;
    }
}
