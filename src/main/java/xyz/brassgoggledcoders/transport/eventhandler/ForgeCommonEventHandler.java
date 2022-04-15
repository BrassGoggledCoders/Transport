package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.util.RailHelper;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.FORGE)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        if (TransportAPI.SHELL_CONTENT_CREATOR.get() instanceof PreparableReloadListener reloadListener) {
            event.addListener(reloadListener);
        }
    }

    @SubscribeEvent
    public static void onRailPlace(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        Player player = rightClickBlock.getPlayer();
        ItemStack itemStack = rightClickBlock.getItemStack();
        if (itemStack.is(ItemTags.RAILS) && itemStack.getItem() instanceof BlockItem railItem) {
            BlockPos railPos = rightClickBlock.getPos();
            Level level = rightClickBlock.getWorld();
            BlockState blockState = level.getBlockState(railPos);

            if (blockState.is(BlockTags.RAILS) && blockState.getBlock() instanceof BaseRailBlock railBlock) {
                RailShape railShape = railBlock.getRailDirection(blockState, level, railPos, null);
                Tuple<Direction, Direction> railDirections = RailHelper.getDirectionsForRailShape(railShape);
                Vec3 hitVec = rightClickBlock.getHitVec().getLocation();
                double y = hitVec.y();
                Direction closest = Direction.getNearest(hitVec.x(), y, hitVec.z());
                while (closest == Direction.DOWN) {
                    y += 0.1;
                    closest = Direction.getNearest(hitVec.x(), y, hitVec.z());
                }
                if (closest.getAxis() != Direction.Axis.Y) {
                    Direction closeRailDirection = null;
                    if (closest == railDirections.getA()) {
                        closeRailDirection = railDirections.getA();
                    } else if (closest== railDirections.getB()) {
                        closeRailDirection = railDirections.getB();
                    }

                    if (closeRailDirection != null) {
                        BlockPos newRail = railPos.offset(closeRailDirection.getNormal());
                        InteractionResult result = railItem.place(new BlockPlaceContext(
                                player,
                                rightClickBlock.getHand(),
                                itemStack,
                                new BlockHitResult(
                                        Vec3.ZERO,
                                        Direction.DOWN,
                                        newRail,
                                        true
                                )
                        ));
                        if (result.consumesAction()) {
                            rightClickBlock.setCanceled(true);
                            player.swing(rightClickBlock.getHand());
                        }
                    }
                }
            }
        }
    }
}
