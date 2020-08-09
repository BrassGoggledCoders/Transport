package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCargoModuleInstance extends CapabilityCargoModuleInstance<IItemHandler> {
    private final InventoryComponent<?> inventory;
    private final LazyOptional<IItemHandler> lazyInventory;

    public ItemCargoModuleInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        this.inventory = new InventoryComponent<>("Inventory", 44, 35, 5);
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
    public int getComparatorLevel() {
        return ItemHandlerHelper.calcRedstoneFromInventory(inventory);
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return inventory.getScreenAddons();
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return inventory.getContainerAddons();
    }
}
