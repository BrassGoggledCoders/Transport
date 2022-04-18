package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
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
import xyz.brassgoggledcoders.transport.util.DirectionHelper;
import xyz.brassgoggledcoders.transport.util.RailHelper;
import xyz.brassgoggledcoders.transport.util.RailPlaceResult;

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
        if (itemStack.is(ItemTags.RAILS)) {
            BlockPos railPos = rightClickBlock.getPos();
            Level level = rightClickBlock.getWorld();
            BlockState blockState = level.getBlockState(railPos);

            if (blockState.is(BlockTags.RAILS) && blockState.getBlock() instanceof BaseRailBlock railBlock) {
                RailShape railShape = railBlock.getRailDirection(blockState, level, railPos, null);
                Direction closest = DirectionHelper.getClosestVerticalSide(rightClickBlock.getHitVec().getLocation());
                if (closest.getAxis() != Direction.Axis.Y) {
                    Direction closeRailDirection = RailHelper.getDirectionForRailShape(railShape, closest, true);

                    if (closeRailDirection != null) {
                        BlockPos newRailPos = RailHelper.findNextPotentialRail(blockState, railPos, closeRailDirection, level);
                        RailPlaceResult placeResult = RailHelper.placeRail(
                                new BlockPlaceContext(
                                        player,
                                        rightClickBlock.getHand(),
                                        itemStack,
                                        new BlockHitResult(
                                                Vec3.atCenterOf(newRailPos),
                                                Direction.UP,
                                                newRailPos,
                                                true
                                        )
                                ),
                                blockState
                        );
                        int attempts = 1;
                        while (!placeResult.consumesAction() && attempts < 8 && closeRailDirection != null) {
                            attempts++;
                            BlockState newRailBlockState = level.getBlockState(placeResult.position());
                            if (newRailBlockState.getBlock() instanceof BaseRailBlock newRailBlock) {
                                RailShape newRailShape = newRailBlock.getRailDirection(newRailBlockState, level, newRailPos, null);
                                Direction newRailDirection = closeRailDirection.getOpposite();
                                closeRailDirection = RailHelper.getDirectionForRailShape(newRailShape, newRailDirection, false);
                                if (closeRailDirection != null) {
                                    newRailPos = RailHelper.findNextPotentialRail(newRailBlockState, newRailPos, closeRailDirection, level);
                                    placeResult = RailHelper.placeRail(new BlockPlaceContext(
                                                    player,
                                                    rightClickBlock.getHand(),
                                                    itemStack,
                                                    new BlockHitResult(
                                                            Vec3.atCenterOf(newRailPos),
                                                            Direction.UP,
                                                            newRailPos,
                                                            true
                                                    )
                                            ),
                                            newRailBlockState
                                    );
                                }
                            }
                        }
                        if (placeResult.consumesAction()) {
                            rightClickBlock.setCanceled(true);
                            player.swing(rightClickBlock.getHand());
                        }
                    }
                }
            }
        }
    }
}
