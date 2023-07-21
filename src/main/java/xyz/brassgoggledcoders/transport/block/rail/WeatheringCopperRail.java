package xyz.brassgoggledcoders.transport.block.rail;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.content.TransportWeathering;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class WeatheringCopperRail extends RailBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringCopperRail(Properties properties, WeatherState weatherState) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return super.getRailMaxSpeed(state, level, pos, cart) / 2;
    }

    @Override
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (state.getValue(this.getShapeProperty()).isAscending() &&
                level.getBlockState(pos.above()).isFaceSturdy(level, pos, Direction.DOWN, SupportType.RIGID)) {
            cart.setDeltaMovement(Vec3.ZERO);
        } else {
            cart.setDeltaMovement(cart.getDeltaMovement().scale(1.2));
        }
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (itemStack.is(Items.HONEYCOMB)) {
            return Optional.ofNullable(TransportWeathering.WAX_ON.get()
                            .get(pState.getBlock())
                    )
                    .map(block -> {
                        if (pPlayer instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pPos, itemStack);
                        }

                        itemStack.shrink(1);

                        BlockState newState = block.withPropertiesOf(pState);
                        pLevel.setBlock(pPos, newState, 11);
                        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, newState));
                        pLevel.levelEvent(pPlayer, 3003, pPos, 0);

                        return InteractionResult.sidedSuccess(pLevel.isClientSide());
                    })
                    .orElse(InteractionResult.PASS);
        }

        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_SCRAPE) {
            Optional<BlockState> blockState = Optional.ofNullable(TransportWeathering.PREVIOUS_BY_BLOCK.get()
                            .get(state.getBlock())
                    )
                    .map(block -> block.withPropertiesOf(state));

            if (blockState.isPresent()) {
                return blockState.get();
            }
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    @Override
    @NotNull
    public Optional<BlockState> getNext(@NotNull BlockState pState) {
        return Optional.of(TransportWeathering.NEXT_BY_BLOCK.get()
                        .get(pState.getBlock())
                )
                .map(block -> block.withPropertiesOf(pState));
    }

    @Override
    @NotNull
    public WeatherState getAge() {
        return this.weatherState;
    }
}
