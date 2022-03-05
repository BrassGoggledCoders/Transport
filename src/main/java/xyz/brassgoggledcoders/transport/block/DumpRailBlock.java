package xyz.brassgoggledcoders.transport.block;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.transport.blockentity.DumpRailBlockEntity;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

public class DumpRailBlock<T> extends BaseRailBlock implements EntityBlock {
    public static final Property<RailShape> RAIL_SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final Capability<T> capability;
    private final BiConsumer<T, T> transferMethod;

    public DumpRailBlock(Properties pProperties, Capability<T> capability, BiConsumer<T, T> transferMethod) {
        super(true, pProperties);
        this.registerDefaultState(this.stateDefinition.any().
                setValue(RAIL_SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE)
        );
        this.capability = capability;
        this.transferMethod = transferMethod;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(RAIL_SHAPE, POWERED, BaseRailBlock.WATERLOGGED);
    }

    @Override
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (!state.getValue(POWERED)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DumpRailBlockEntity dumpRailBlockEntity) {
                dumpRailBlockEntity.tryDump(cart, capability, transferMethod);
            }
        }
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
    public Property<RailShape> getShapeProperty() {
        return RAIL_SHAPE;
    }

    @Nonnull
    public static DumpRailBlock<IItemHandler> itemDumpRail(Properties properties) {
        return new DumpRailBlock<>(
                properties,
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                (from, to) -> {
                    int slots = Math.min(from.getSlots(), 128);
                    for (int slotNumber = 0; slotNumber < slots; slotNumber++) {
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
                }
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TransportBlocks.ITEM_DUMP_RAIL.getSibling(ForgeRegistries.BLOCK_ENTITIES)
                .map(type -> type.create(pPos, pState))
                .orElse(null);
    }
}
