package xyz.brassgoggledcoders.transport.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BuoyBlockItem extends BlockItem {
    public BuoyBlockItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Nonnull
    @Override
    public ActionResultType onItemUse(@Nonnull ItemUseContext context) {
        return ActionResultType.PASS;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        BlockRayTraceResult fluidRayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        BlockRayTraceResult blockRayTraceResult = fluidRayTraceResult.withPosition(fluidRayTraceResult.getPos().up());
        ActionResultType actionResultType = super.onItemUse(new ItemUseContext(player, hand, blockRayTraceResult));
        return new ActionResult<>(actionResultType, player.getHeldItem(hand));
    }
}
