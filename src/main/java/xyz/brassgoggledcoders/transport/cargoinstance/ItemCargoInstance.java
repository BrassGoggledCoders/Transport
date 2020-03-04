package xyz.brassgoggledcoders.transport.cargoinstance;

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
import xyz.brassgoggledcoders.transport.container.cargo.ScreenAddonCargoContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCargoInstance extends CargoInstance {
    private final InventoryComponent<?> inventory;
    private final LazyOptional<IItemHandler> lazyInventory;

    public ItemCargoInstance(Cargo cargo) {
        super(cargo);
        this.inventory = new InventoryComponent<>("Inventory", 71, 35, 5);
        this.lazyInventory = LazyOptional.of(() -> inventory);
    }

    @Override
    public boolean onInteraction(PlayerEntity entityPlayer, Hand hand) {
        if (!entityPlayer.isCrouching()) {

        }
        return super.onInteraction(entityPlayer, hand);
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        super.deserializeNBT(compoundNBT);
        inventory.deserializeNBT(compoundNBT.getCompound("inventory"));
    }

    @Nonnull
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.put("inventory", inventory.serializeNBT());
        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyInventory.cast();
        } else {
            return super.getCapability(cap, direction);
        }
    }
}
