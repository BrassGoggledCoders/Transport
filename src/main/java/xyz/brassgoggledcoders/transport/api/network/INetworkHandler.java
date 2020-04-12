package xyz.brassgoggledcoders.transport.api.network;

import xyz.brassgoggledcoders.transport.api.module.IModularEntity;
import xyz.brassgoggledcoders.transport.api.module.ModuleInstance;

public interface INetworkHandler {
    void sendModuleCaseUpdate(IModularEntity entity, ModuleInstance<?> changed, boolean added);
}
