package xyz.brassgoggledcoders.transport.tileentity;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;

import java.util.List;

public class ModuleConfiguratorTileEntity extends TileEntity implements IComponentHarness, IContainerAddonProvider,
        IScreenAddonProvider {
    private final InventoryComponent<ModuleConfiguratorTileEntity> modularItemInventory;
    private final InventoryComponent<ModuleConfiguratorTileEntity> moduleInventory;

    public ModuleConfiguratorTileEntity() {
        this(TransportBlocks.MODULE_CONFIGURATOR.getTileEntityType());
    }

    public ModuleConfiguratorTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.modularItemInventory = new InventoryComponent<>("modular_item", 134, 107, 1);
        this.moduleInventory = new InventoryComponent<>("module", 8,18, 8);
    }

    @Override
    public World getComponentWorld() {
        return this.world;
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {

    }

    @Override
    public void markComponentDirty() {
        this.markDirty();
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> screenAddons = Lists.newArrayList();
        screenAddons.addAll(modularItemInventory.getScreenAddons());
        screenAddons.addAll(moduleInventory.getScreenAddons());
        return screenAddons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddon = Lists.newArrayList();
        containerAddon.addAll(modularItemInventory.getContainerAddons());
        containerAddon.addAll(moduleInventory.getContainerAddons());
        return containerAddon;
    }
}
