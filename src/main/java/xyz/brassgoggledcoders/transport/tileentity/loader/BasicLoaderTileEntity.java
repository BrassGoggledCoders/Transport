package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.google.common.collect.Maps;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.brassgoggledcoders.transport.block.loader.LoadType;
import xyz.brassgoggledcoders.transport.block.loader.LoaderBlock;
import xyz.brassgoggledcoders.transport.container.LoaderContainerProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class BasicLoaderTileEntity<CAP> extends TileEntity implements ITickableTileEntity, IComponentHarness,
        IScreenAddonProvider, IContainerAddonProvider {

    private final Capability<CAP> capability;
    private final EnumMap<Direction, LazyOptional<CAP>> lazyOptionals;
    private final BiConsumer<CAP, CAP> transfer;
    private int run = 20;

    public <T extends BasicLoaderTileEntity<CAP>> BasicLoaderTileEntity(TileEntityType<T> tileEntityType,
                                                                        Capability<CAP> capability,
                                                                        BiConsumer<CAP, CAP> transfer) {
        super(tileEntityType);
        this.capability = capability;
        this.transfer = transfer;
        this.lazyOptionals = Maps.newEnumMap(Direction.class);
    }

    @Override
    public void tick() {
        if (this.getWorld() != null) {
            if (!this.getWorld().isRemote()) {
                if (run >= 0) {
                    run--;
                } else {
                    doWork();
                    run = 15 + this.getWorld().getRandom().nextInt(10);
                }
            }

        }

    }

    @Nonnull
    public World getTheWorld() {
        return Objects.requireNonNull(this.getWorld());
    }

    private void doWork() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getPos()).grow(1);
        List<Entity> entities = this.getTheWorld().getEntitiesInAABBexcluding(null, axisAlignedBB, Entity::isAlive);

        for (Direction side : Direction.values()) {
            BlockPos neighborPos = this.getPos().offset(side, 1);
            LoadType loadType = this.getBlockState().get(LoaderBlock.PROPERTIES.get(side));
            if (loadType != LoadType.NONE) {
                doWorkOnSide(loadType, side, neighborPos, entities.stream().filter(entity -> entity.getPosition().equals(neighborPos)));
            }
        }
    }

    private void doWorkOnSide(LoadType loadType, Direction side, BlockPos neighborPos, Stream<Entity> entitiesOnSide) {
        this.getNeighborCap(side, neighborPos, entitiesOnSide)
                .ifPresent(cap -> this.handleNeighborCap(loadType, cap));
    }

    private void handleNeighborCap(LoadType loadType, CAP cap) {
        if (loadType == LoadType.INPUT) {
            this.getInternalCAP().ifPresent(internal -> transfer(cap, internal));
        } else if (loadType == LoadType.OUTPUT) {
            this.getInternalCAP().ifPresent(internal -> transfer(internal, cap));
        }
    }

    protected void transfer(CAP from, CAP to) {
        transfer.accept(from, to);
    }

    private LazyOptional<CAP> getNeighborCap(Direction side, BlockPos neighborPos, Stream<Entity> entitiesOnSide) {
        LazyOptional<CAP> capLazyOptional;
        Optional<Entity> entity = entitiesOnSide.findAny();
        if (entity.isPresent()) {
            capLazyOptional = entity.map(value -> value.getCapability(this.capability, side.getOpposite()))
                    .orElseGet(LazyOptional::empty);
        } else {
            capLazyOptional = Optional.ofNullable(this.getTheWorld().getTileEntity(neighborPos))
                    .map(tileEntity -> tileEntity.getCapability(this.capability, side.getOpposite()))
                    .orElseGet(LazyOptional::empty);
        }

        return capLazyOptional;
    }

    public void updateSide(Direction direction) {
        LazyOptional<CAP> existing = lazyOptionals.put(direction, this.getNewCAPForSide(direction));
        if (existing != null && existing.isPresent()) {
            existing.invalidate();
        }
    }

    private LazyOptional<CAP> getNewCAPForSide(Direction direction) {
        LoadType loadType = this.getBlockState().get(LoaderBlock.PROPERTIES.get(direction));
        switch (loadType) {
            case NONE:
                return LazyOptional.empty();
            case INPUT:
                return this.createInputCAP();
            case OUTPUT:
                return this.createOutputCAP();
        }
        return LazyOptional.empty();
    }

    @Override
    public World getComponentWorld() {
        return this.getWorld();
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {

    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == this.capability) {
            if (side != null) {
                LazyOptional<CAP> lazyOptional = lazyOptionals.get(side);
                if (lazyOptional != null && lazyOptional.isPresent()) {
                    return lazyOptional.cast();
                }
            } else {
                return this.getInternalCAP().cast();
            }
        }

        return super.getCapability(cap, side);
    }

    protected abstract LazyOptional<CAP> getInternalCAP();

    protected abstract LazyOptional<CAP> createOutputCAP();

    protected abstract LazyOptional<CAP> createInputCAP();

    protected abstract CompoundNBT serializeCap();

    protected abstract void deserializeCap(CompoundNBT compoundNBT);

    public void onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, new LoaderContainerProvider(this),
                    packetBuffer -> LocatorFactory.writePacketBuffer(packetBuffer, new TileEntityLocatorInstance(this.getPos())));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState blockState, CompoundNBT nbt) {
        super.read(blockState, nbt);
        this.deserializeCap(nbt.getCompound("capability"));
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        CompoundNBT superNBT = super.write(nbt);
        superNBT.put("capability", this.serializeCap());
        return superNBT;
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.getInternalCAP().invalidate();
        this.lazyOptionals.forEach(((direction, capLazyOptional) -> capLazyOptional.invalidate()));
    }
}
