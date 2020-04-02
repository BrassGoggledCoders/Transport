package xyz.brassgoggledcoders.transport.container;

import com.hrznstudio.titanium.container.BasicAddonContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.entity.EntityWorldPosCallable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModuleContainerProvider implements INamedContainerProvider {
    private final ModuleInstance<?> moduleInstance;
    private final IModularEntity modularEntity;

    public ModuleContainerProvider(ModuleInstance<?> moduleInstance, IModularEntity modularEntity) {
        this.moduleInstance = moduleInstance;
        this.modularEntity = modularEntity;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return moduleInstance.getDisplayName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BasicAddonContainer(moduleInstance, TransportContainers.MODULE.get(),
                new EntityWorldPosCallable(modularEntity.getSelf()), playerInventory, windowId);
    }
}
