package xyz.brassgoggledcoders.transport.loading;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.loading.IEntityBlockLoading;
import xyz.brassgoggledcoders.transport.api.loading.ILoading;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.content.TransportModuleSlots;
import xyz.brassgoggledcoders.transport.content.TransportModuleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ModularVehicleLoading implements IEntityBlockLoading {
    private final List<EntityType<?>> entities;

    public ModularVehicleLoading(EntityType<?>... entityType) {
        this.entities = Arrays.asList(entityType);
    }

    @Override
    public boolean attemptLoad(@Nonnull ILoading loading, @Nonnull Entity entity, @Nonnull BlockState blockState, @Nullable TileEntity tileEntity) {
        return entity.getCapability(TransportAPI.MODULAR_ENTITY)
                .resolve()
                .map(modularEntity -> {
                    Module<?> module = TransportAPI.getModuleFromItem(blockState.getBlock().asItem());
                    if (module != null) {
                        ModuleInstance<?> moduleInstance = modularEntity.add(module, TransportModuleSlots.CARGO.get(), true);
                        if (moduleInstance instanceof CargoModuleInstance && tileEntity != null) {
                            ((CargoModuleInstance) moduleInstance).fromTileEntity(tileEntity);
                        }
                        return moduleInstance != null;
                    } else {
                        return false;
                    }
                })
                .orElse(false);
    }

    @Nullable
    @Override
    public Pair<BlockState, TileEntity> attemptUnload(@Nonnull Entity entity) {
        return entity.getCapability(TransportAPI.MODULAR_ENTITY)
                .resolve()
                .<CargoModuleInstance>map(modularEntity -> modularEntity.getModuleInstance(TransportModuleTypes.CARGO.get()))
                .map(cargoInstance -> Pair.of(cargoInstance.getBlockState(), cargoInstance.asTileEntity()))
                .orElse(null);
    }

    @Override
    public void unload(@Nonnull ILoading loading, @Nonnull Entity entity) {
        entity.getCapability(TransportAPI.MODULAR_ENTITY)
                .ifPresent(modularEntity -> modularEntity.remove(TransportModuleSlots.CARGO.get(), true));
    }

    @Override
    public Collection<EntityType<?>> getSupportedEntities() {
        return entities;
    }
}
