package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicLoaderTileEntity<CAP, IMPL extends CAP> extends TileEntity
        implements ITickableTileEntity, IComponentHarness {

    private final Capability<CAP> capability;
    private final IMPL implementation;
    private final LazyOptional<CAP> lazyOptional;

    public <T extends BasicLoaderTileEntity<CAP, IMPL>> BasicLoaderTileEntity(TileEntityType<T> tileEntityType,
                                                                              Capability<CAP> capability,
                                                                              IMPL implementation) {
        super(tileEntityType);
        this.capability = capability;
        this.implementation = implementation;
        this.lazyOptional = LazyOptional.of(() -> implementation);
    }

    @Override
    public void tick() {

    }

    @Override
    public World getComponentWorld() {
        return this.getWorld();
    }

    @Override
    public void markComponentForUpdate() {

    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == this.capability) {
            return this.lazyOptional.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }
}
