package xyz.brassgoggledcoders.transport.api.network;

import net.minecraft.nbt.CompoundNBT;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;

public interface INetworkHandler {
    void sendAddModuleCase(IModularEntity entity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot);

    void sendModuleInstanceUpdate(IModularEntity entity, ModuleSlot moduleSlot, int type, CompoundNBT updateInfo);
}
