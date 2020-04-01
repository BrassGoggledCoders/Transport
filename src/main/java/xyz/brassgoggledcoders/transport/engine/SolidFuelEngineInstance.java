package xyz.brassgoggledcoders.transport.engine;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.engine.EngineInstance;
import xyz.brassgoggledcoders.transport.api.engine.PoweredState;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SolidFuelEngineInstance extends EngineInstance implements IScreenAddonProvider, IContainerAddonProvider {
    private final InventoryComponent<?> itemStackHandler;
    private LazyOptional<IItemHandler> optionalItemHandler;
    private int burnTime = 0;

    public SolidFuelEngineInstance(Engine engine, IModularEntity powered) {
        super(engine, powered);

        this.itemStackHandler = new InventoryComponent<>("engine", 80, 35, 1)
                .setInputFilter((stack, slot) -> ForgeHooks.getBurnTime(stack) > 0);
        this.optionalItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    public ActionResultType applyInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        this.getModularEntity().openContainer(player, this, packetBuffer -> {
        });
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isRunning() {
        return burnTime > 0;
    }

    @Override
    public void tick() {
        if (burnTime > 0) {
            switch (this.getPoweredState()) {
                case RUNNING:
                    burnTime--;
                case IDLE:
                    burnTime--;
                    break;
                default:
                    break;
            }
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
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return itemStackHandler.getScreenAddons();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return itemStackHandler.getContainerAddons();
    }
}
