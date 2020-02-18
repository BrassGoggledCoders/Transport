package xyz.brassgoggledcoders.transport.block.loader;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;

public abstract class BasicLoaderBlock extends Block {
    public static final EnumMap<Direction, EnumProperty<LoadType>> PROPERTIES = createLoadTypeProperties();

    protected BasicLoaderBlock(Block.Properties properties) {
        super(properties);
        BlockState defaultState = this.getStateContainer().getBaseState();
        for (EnumProperty<LoadType> loadTypeEnumProperty : PROPERTIES.values()) {
            defaultState = defaultState.with(loadTypeEnumProperty, LoadType.NONE);
        }
        this.setDefaultState(defaultState);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        PROPERTIES.values().forEach(builder::add);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItemStack = player.getHeldItem(hand);
        if (heldItemStack.getItem().isIn(TransportItemTags.TOOL)) {
            world.setBlockState(pos, state.with(PROPERTIES.get(rayTraceResult.getFace()),
                    state.get(PROPERTIES.get(rayTraceResult.getFace())).next()));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    private static EnumMap<Direction, EnumProperty<LoadType>> createLoadTypeProperties() {
        EnumMap<Direction, EnumProperty<LoadType>> loadTypes = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            loadTypes.put(direction, EnumProperty.create(direction.name().toLowerCase(), LoadType.class));
        }
        return loadTypes;
    }
}
