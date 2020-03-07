package xyz.brassgoggledcoders.transport.cargoinstance.capabilitycargoinstance;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.cargoinstance.CargoInstance;
import xyz.brassgoggledcoders.transport.capability.InventoryPlusComponent;
import xyz.brassgoggledcoders.transport.container.containeraddon.IContainerAddon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ItemCargoInstance extends CapabilityCargoInstance<IItemHandler> {
    private final InventoryPlusComponent<?> inventory;
    private final LazyOptional<IItemHandler> lazyInventory;

    public ItemCargoInstance(Cargo cargo) {
        super(cargo, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.inventory = new InventoryPlusComponent<>("Inventory", 44, 35, 5);
        this.lazyInventory = LazyOptional.of(() -> inventory);
    }

    @Override
    protected LazyOptional<IItemHandler> getLazyOptional() {
        return lazyInventory;
    }

    @Override
    protected CompoundNBT serializeCapability() {
        return inventory.serializeNBT();
    }

    @Override
    protected void deserializeCapability(CompoundNBT nbt) {
        inventory.deserializeNBT(nbt);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return inventory.getScreenAddons();
    }

    @Override
    public List<IContainerAddon> getContainerAddons() {
        return Collections.singletonList(inventory);
    }
}
