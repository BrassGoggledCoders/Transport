package xyz.brassgoggledcoders.transport.engine;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.engine.EngineModuleInstance;
import xyz.brassgoggledcoders.transport.api.engine.PoweredState;
import xyz.brassgoggledcoders.transport.api.entity.EntityWorldPosCallable;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.capability.itemhandler.FuelItemStackHandler;
import xyz.brassgoggledcoders.transport.container.module.engine.SolidFuelModuleContainer;
import xyz.brassgoggledcoders.transport.content.TransportContainers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolidFuelEngineModuleInstance extends EngineModuleInstance {
    private final FuelItemStackHandler itemStackHandler;
    private final LazyOptional<IItemHandler> optionalItemHandler;

    private int maxBurnTime = 0;
    private int burnTime = 0;

    public SolidFuelEngineModuleInstance(EngineModule engineModule, IModularEntity powered) {
        super(engineModule, powered);

        this.itemStackHandler = new FuelItemStackHandler(1);
        this.optionalItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public ActionResultType applyInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if (!player.isCrouching()) {
            this.getModularEntity().openModuleContainer(this, player);
            return ActionResultType.func_233537_a_(this.getModularEntity().getTheWorld().isRemote());
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean isRunning() {
        return burnTime > 0;
    }

    @Override
    public void tick() {
        if (!this.getModularEntity().getTheWorld().isRemote()) {
            if (burnTime > 0) {
                burnTime -= this.getPoweredState().getBurnAmount();
            }

            if (burnTime <= 0) {
                burnTime = 0;
                if (this.getPoweredState() == PoweredState.RUNNING) {
                    ItemStack itemStack = itemStackHandler.getStackInSlot(0);
                    if (!itemStack.isEmpty()) {
                        int newBurnTime = ForgeHooks.getBurnTime(itemStack) * 2;
                        if (newBurnTime > 0) {
                            if (itemStack.hasContainerItem()) {
                                itemStackHandler.setStackInSlot(0, itemStack.getContainerItem());
                            } else {
                                itemStack.shrink(1);
                            }
                            burnTime += newBurnTime;
                            maxBurnTime = newBurnTime;
                        }
                    }
                }
            }
        }

        this.handleParticles(ParticleTypes.LARGE_SMOKE, 4);
    }

    @Override
    public double getMaximumSpeed() {
        return 0.25D;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap ? optionalItemHandler.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = super.serializeNBT();
        compoundNBT.putInt("burnTime", this.burnTime);
        compoundNBT.putInt("maxBurnTime", this.maxBurnTime);
        compoundNBT.put("itemStackHandler", itemStackHandler.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.burnTime = nbt.getInt("burnTime");
        this.maxBurnTime = nbt.getInt("maxBurnTime");
        this.itemStackHandler.deserializeNBT(nbt.getCompound("itemStackHandler"));
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        this.optionalItemHandler.invalidate();
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    @Nullable
    @Override
    public Function3<Integer, PlayerInventory, PlayerEntity, ? extends Container> getContainerCreator() {
        return (id, playerInventory, playerEntity) -> new SolidFuelModuleContainer(
                TransportContainers.SOLID_FUEL_MODULE.get(),
                id,
                playerInventory,
                new EntityWorldPosCallable(this.getModularEntity().getSelf()),
                this.itemStackHandler,
                this::getBurnTime,
                this::getMaxBurnTime
        );
    }
}
