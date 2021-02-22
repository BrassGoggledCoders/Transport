package xyz.brassgoggledcoders.transport.container.modular;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.entity.EntityWorldPosCallable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class ModuleContainerProvider implements INamedContainerProvider {
    private final ModuleInstance<?> moduleInstance;
    private final IModularEntity modularEntity;

    public ModuleContainerProvider(@Nullable ModuleInstance<?> moduleInstance, IModularEntity modularEntity) {
        this.moduleInstance = moduleInstance;
        this.modularEntity = modularEntity;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        if (moduleInstance != null) {
            return moduleInstance.getDisplayName()
                    .copyRaw()
                    .mergeStyle(TextFormatting.BLACK);
        } else {
            return modularEntity.getSelf().getDisplayName();
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        ModularContainer modularContainer = new ModularContainer(
                TransportContainers.MODULE.get(),
                windowId,
                playerInventory,
                new EntityWorldPosCallable(modularEntity.getSelf()),
                LazyOptional.of(() -> modularEntity)
        );
        if (moduleInstance != null) {
            modularContainer.setActiveTab(moduleInstance);
        }
        return modularContainer;
    }


}
