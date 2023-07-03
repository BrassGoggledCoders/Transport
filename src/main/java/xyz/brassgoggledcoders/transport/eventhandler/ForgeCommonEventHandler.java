package xyz.brassgoggledcoders.transport.eventhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.crafting.conditions.ConditionContext;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.capability.IRailProvider;
import xyz.brassgoggledcoders.transport.api.tag.TransportItemTags;
import xyz.brassgoggledcoders.transport.service.ShellContentCreatorServiceImpl;
import xyz.brassgoggledcoders.transport.util.DirectionHelper;
import xyz.brassgoggledcoders.transport.util.RailHelper;
import xyz.brassgoggledcoders.transport.util.RailPlaceResult;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.FORGE)
public class ForgeCommonEventHandler {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        if (TransportAPI.SHELL_CONTENT_CREATOR.get() instanceof ShellContentCreatorServiceImpl reloadListener) {
            ICondition.IContext context = event.getServerResources()
                    .listeners()
                    .stream()
                    .filter(TagManager.class::isInstance)
                    .map(TagManager.class::cast)
                    .findFirst()
                    .<ICondition.IContext>map(ConditionContext::new)
                    .orElse(ConditionContext.EMPTY);
            reloadListener.setContext(context);
            event.addListener(reloadListener);
        }
    }

    @SubscribeEvent
    public static void onRailPlace(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        Player player = rightClickBlock.getPlayer();
        ItemStack itemStack = rightClickBlock.getItemStack();

        ItemStack railStack;
        Runnable afterPlace = () -> {

        };

        if (itemStack.is(ItemTags.RAILS)) {
            railStack = itemStack;
        } else if (itemStack.is(TransportItemTags.RAIL_PROVIDERS)) {
            LazyOptional<IRailProvider> railProvider = itemStack.getCapability(IRailProvider.CAPABILITY);

            railStack = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .resolve()
                    .flatMap(inventory -> railProvider.map(capability -> capability.findNext(inventory, false)))
                    .orElse(ItemStack.EMPTY);
            afterPlace = () -> railProvider.ifPresent(IRailProvider::nextPosition);
        } else {
            railStack = ItemStack.EMPTY;
        }

        if (!railStack.isEmpty()) {
            BlockPos railPos = rightClickBlock.getPos();
            Level level = rightClickBlock.getWorld();
            BlockState blockState = level.getBlockState(railPos);

            if (!railStack.isEmpty() && blockState.is(BlockTags.RAILS) && blockState.getBlock() instanceof BaseRailBlock railBlock) {
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
                                        railStack,
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
                                                    railStack,
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
                            afterPlace.run();
                            player.swing(rightClickBlock.getHand());
                        }
                    }
                }
            }
        }
    }
}
