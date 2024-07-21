package xyz.brassgoggledcoders.transport.block.rail;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.blockentity.rail.LoadingRailBlockEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

public class LoadingRailBlock<T> extends BaseRailBlock implements EntityBlock {
    public static final Property<RailShape> RAIL_SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final EntityCapability<T, Direction> entityCapability;
    private final BlockCapability<T, Direction> blockCapability;
    private final MapCodec<LoadingRailBlock<T>> codec;
    private final Function3<T, T, Pair<Integer, Integer>, Pair<Integer, Integer>> transferMethod;
    private final boolean loading;

    public LoadingRailBlock(Properties pProperties, boolean loading, EntityCapability<T, Direction> entityCapability,
                            BlockCapability<T, Direction> blockCapability, Function<Properties, LoadingRailBlock<T>> codec,
                            Function3<T, T, Pair<Integer, Integer>, Pair<Integer, Integer>> transferMethod) {
        super(true, pProperties);
        this.codec = simpleCodec(codec);
        this.blockCapability = blockCapability;
        this.registerDefaultState(this.stateDefinition.any().
                setValue(RAIL_SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
        this.loading = loading;
        this.entityCapability = entityCapability;
        this.transferMethod = transferMethod;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(RAIL_SHAPE, POWERED, BaseRailBlock.WATERLOGGED);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (!state.getValue(POWERED) && !level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LoadingRailBlockEntity loadingRailBlockEntity) {
                loadingRailBlockEntity.tryLoading(cart, loading, entityCapability, blockCapability, transferMethod);
            }
        }
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseRailBlock> codec() {
        return this.codec;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void updateState(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock) {
        boolean poweredState = pState.getValue(POWERED);
        boolean hasPower = pLevel.hasNeighborSignal(pPos);
        if (hasPower != poweredState) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, hasPower), 3);
        }
    }

    @Override
    @Nonnull
    @Deprecated
    public Property<RailShape> getShapeProperty() {
        return RAIL_SHAPE;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TransportBlocks.LOADING_RAIL_BLOCK_ENTITY.get()
                .create(pPos, pState);
    }

    @Nonnull
    public static LoadingRailBlock<IItemHandler> itemLoadingRail(Properties properties, boolean loading) {
        return new LoadingRailBlock<>(
                properties,
                loading,
                ItemHandler.ENTITY_AUTOMATION,
                ItemHandler.BLOCK,
                (value) -> itemLoadingRail(value, loading),
                (from, to, index) -> {
                    int currentSlot = index.getSecond();
                    int maxSlot = Math.min(from.getSlots(), currentSlot + index.getFirst());
                    for (int slotNumber = currentSlot; slotNumber < maxSlot; slotNumber++) {
                        ItemStack itemStack = from.extractItem(slotNumber, 64, true);
                        if (!itemStack.isEmpty()) {
                            ItemStack notInserted = ItemHandlerHelper.insertItem(to, itemStack, true);
                            if (notInserted.getCount() != itemStack.getCount()) {
                                int inserted = itemStack.getCount() - notInserted.getCount();
                                ItemStack movedItemStack = from.extractItem(slotNumber, inserted, false);
                                ItemHandlerHelper.insertItem(to, movedItemStack, false);
                            }
                        }
                    }
                    return maxSlot == from.getSlots() ? Pair.of(index.getFirst() - (maxSlot - currentSlot), 0) :
                            Pair.of(0, maxSlot);
                }
        );
    }

    @Nonnull
    public static LoadingRailBlock<IFluidHandler> fluidDumpRail(Properties properties, boolean loading) {
        return new LoadingRailBlock<>(
                properties,
                loading,
                Capabilities.FluidHandler.ENTITY,
                Capabilities.FluidHandler.BLOCK,
                (value) -> fluidDumpRail(value, loading),
                (from, to, index) -> {
                    FluidStack output = from.drain(FluidType.BUCKET_VOLUME * index.getFirst(), FluidAction.SIMULATE);
                    if (!output.isEmpty()) {
                        int filledAmount = to.fill(output, FluidAction.SIMULATE);
                        if (filledAmount > 0) {
                            to.fill(from.drain(filledAmount, FluidAction.EXECUTE), FluidAction.EXECUTE);
                            if (output.getAmount() == filledAmount && filledAmount > 1000) {
                                return Pair.of(0, 1);
                            }
                        }
                    }
                    return Pair.of(index.getFirst() - (output.getAmount() / 1000), 0);
                }
        );
    }

    @NotNull
    public static LoadingRailBlock<IEnergyStorage> energyDumpRail(Properties properties, boolean loading) {
        return new LoadingRailBlock<>(
                properties,
                loading,
                EnergyStorage.ENTITY,
                EnergyStorage.BLOCK,
                (value) -> energyDumpRail(value, loading),
                (from, to, index) -> {
                    int output = from.extractEnergy(1000 * index.getFirst(), true);
                    if (output > 0) {
                        int filledAmount = to.receiveEnergy(output, true);
                        if (filledAmount > 0) {
                            to.receiveEnergy(from.extractEnergy(filledAmount, false), false);
                            if (output == filledAmount && filledAmount >= 1000) {
                                return Pair.of(0, 1);
                            }
                        }
                    }
                    return Pair.of(index.getFirst() - (output / 1000), 0);
                }
        );
    }
}
