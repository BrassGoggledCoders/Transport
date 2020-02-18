package xyz.brassgoggledcoders.transport.tileentity.loader;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.sideness.ICapabilityHolder;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class BasicLoaderTileEntity<CAP extends ICapabilityHolder<?, ?>> extends TileEntity
        implements ITickableTileEntity, IComponentHarness {
    private final CAP capabilityHolder;

    public <T extends BasicLoaderTileEntity<?>> BasicLoaderTileEntity(TileEntityType<T> tileEntityType, CAP capabilityHolder) {
        super(tileEntityType);
        this.capabilityHolder = capabilityHolder;
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
}
