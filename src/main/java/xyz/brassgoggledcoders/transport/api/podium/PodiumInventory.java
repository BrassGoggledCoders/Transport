package xyz.brassgoggledcoders.transport.api.podium;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PodiumInventory extends Inventory {
    private final Predicate<PlayerEntity> isUsableByPlayer;

    public PodiumInventory(ItemStack itemStack, Predicate<PlayerEntity> isUsableByPlayer) {
        super(itemStack);
        this.isUsableByPlayer = isUsableByPlayer;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return !this.getStackInSlot(0).isEmpty() && isUsableByPlayer.test(player);
    }
}
