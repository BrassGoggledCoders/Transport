package xyz.brassgoggledcoders.transport.block.loader;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.container.LoaderContainerProvider;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.tileentity.loader.BasicLoaderTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Consumer;

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
            handleTileEntity(world, pos, basicLoaderTileEntity -> basicLoaderTileEntity.updateSide(rayTraceResult.getFace()));
            return ActionResultType.SUCCESS;
        } else if (!player.isCrouching()) {
            if (player instanceof ServerPlayerEntity) {
                handleTileEntity(world, pos, basicLoaderTileEntity -> NetworkHooks.openGui((ServerPlayerEntity) player,
                        new LoaderContainerProvider(this, basicLoaderTileEntity),
                        packetBuffer -> packetBuffer.writeBlockPos(pos)));
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @SuppressWarnings("rawtypes")
    private void handleTileEntity(World world, BlockPos pos, Consumer<BasicLoaderTileEntity> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof BasicLoaderTileEntity)
                .map(BasicLoaderTileEntity.class::cast)
                .ifPresent(tileEntityConsumer);
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

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getTranslationKey());
    }
}
