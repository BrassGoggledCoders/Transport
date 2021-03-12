package xyz.brassgoggledcoders.transport.cargoinstance.capability;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.container.loader.ItemLoaderContainer;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> getContainerCreator() {
        return (id, playerInventory, playerEntity) -> new ItemLoaderContainer(
                TransportContainers.ITEM_LOADER.get(),
                id,
                playerInventory,
                new EntityWorldPosCallable(
                        this.getModularEntity().getSelf()
                ),
                this.inventory
        );
    }
}
