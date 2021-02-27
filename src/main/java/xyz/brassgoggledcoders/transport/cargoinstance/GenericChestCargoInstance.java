package xyz.brassgoggledcoders.transport.cargoinstance;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleTab;
import xyz.brassgoggledcoders.transport.container.module.cargo.ChestModuleContainer;
import xyz.brassgoggledcoders.transport.screen.module.cargo.ChestModuleScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GenericChestCargoInstance extends CargoModuleInstance {
    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> lazyInventory;

    public GenericChestCargoInstance(CargoModule cargoModule, IModularEntity modularEntity) {
        super(cargoModule, modularEntity);
        this.inventory = new ItemStackHandler(27);
        this.lazyInventory = LazyOptional.of(() -> this.inventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyInventory.cast() : super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        compoundNBT.put("inventory", inventory.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public int getComparatorLevel() {
        return ItemHandlerHelper.calcRedstoneFromInventory(inventory);
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if (!player.isCrouching()) {
            this.getModularEntity().openModuleContainer(this, player);
            return ActionResultType.SUCCESS;
        }
        return super.applyInteraction(player, vec, hand);
    }

    @Nullable
    @Override
    public ModuleTab<?> createTab() {
        return new ModuleTab<>(
                this.getDisplayName(),
                this::asItemStack,
                container -> new ChestModuleContainer(container, 3, inventory),
                () -> ChestModuleScreen::new
        );
    }
}
