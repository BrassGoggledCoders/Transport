package xyz.brassgoggledcoders.transport.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.container.navigation.NavigationChartContainer;
import xyz.brassgoggledcoders.transport.container.provider.GenericContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class NavigationChartItem extends Item {
    public NavigationChartItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new GenericContainerProvider(
                    () -> this.getDisplayName(itemStack), NavigationChartContainer::create)
            );
        }
        return ActionResult.func_233538_a_(itemStack, world.isRemote());
    }
}
