package xyz.brassgoggledcoders.transport.container.jobsite;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

public class SingleRecipeOutputSlot extends Slot {
    private final IWorldPosCallable worldPosCallable;
    private final BooleanSupplier craft;
    private long lastOnTake;

    public SingleRecipeOutputSlot(IInventory inventory, int index, int xPosition, int yPosition,
                                  IWorldPosCallable worldPosCallable, BooleanSupplier craft) {
        super(inventory, index, xPosition, yPosition);
        this.worldPosCallable = worldPosCallable;
        this.craft = craft;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        if (this.craft.getAsBoolean()) {
            stack.getItem().onCreated(stack, thePlayer.world, thePlayer);
            worldPosCallable.consume((world, blockPos) -> {
                long l = world.getGameTime();
                if (lastOnTake != l) {
                    world.playSound(null, blockPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                    lastOnTake = l;
                }

            });
            return super.onTake(thePlayer, stack);
        } else {
            return ItemStack.EMPTY;
        }
    }
}
