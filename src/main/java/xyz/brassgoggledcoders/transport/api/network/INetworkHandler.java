package xyz.brassgoggledcoders.transport.api.network;

import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;
import xyz.brassgoggledcoders.transport.api.module.slot.ModuleSlot;

public interface INetworkHandler {
    void sendAddModuleCase(IModularEntity entity, ModuleInstance<?> moduleInstance, ModuleSlot moduleSlot);
}
