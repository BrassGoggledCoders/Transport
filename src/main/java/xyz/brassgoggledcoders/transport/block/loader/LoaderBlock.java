package xyz.brassgoggledcoders.transport.block.loader;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.container.loader.LoaderContainerProvider;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.tileentity.loader.BasicLoaderTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoaderBlock extends Block {
    public static final EnumMap<Direction, EnumProperty<LoadType>> PROPERTIES = createLoadTypeProperties();
    private final Supplier<? extends BasicLoaderTileEntity<?>> tileSupplier;

    public LoaderBlock(Supplier<? extends BasicLoaderTileEntity<?>> tileSupplier) {
        this(Properties.create(Material.IRON)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.METAL), tileSupplier);
    }

    public LoaderBlock(Properties properties, Supplier<? extends BasicLoaderTileEntity<?>> tileSupplier) {
        super(properties);
        this.tileSupplier = tileSupplier;
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
        if (heldItemStack.getItem().isIn(TransportItemTags.TOOLS)) {
            world.setBlockState(pos, state.cycle(PROPERTIES.get(rayTraceResult.getFace())));
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
    private void handleTileEntity(IWorld world, BlockPos pos, Consumer<BasicLoaderTileEntity> tileEntityConsumer) {
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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileSupplier.get();
    }

    private static EnumMap<Direction, EnumProperty<LoadType>> createLoadTypeProperties() {
        EnumMap<Direction, EnumProperty<LoadType>> loadTypes = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            loadTypes.put(direction, EnumProperty.create(direction.name().toLowerCase(), LoadType.class));
        }
        return loadTypes;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(@Nonnull BlockState state, Direction facing, BlockState facingState,
                                          IWorld world, BlockPos currentPos, BlockPos facingPos) {
        this.handleTileEntity(world, currentPos, basicLoaderTileEntity -> basicLoaderTileEntity.updateSide(facing));
        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getTranslationKey());
    }
}