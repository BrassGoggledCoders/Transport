package xyz.brassgoggledcoders.transport.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import xyz.brassgoggledcoders.transport.api.manager.IManager;
import xyz.brassgoggledcoders.transport.api.manager.IWorker;

public class TransportCapabilities {

    @CapabilityInject(IManager.class)
    public static Capability<IManager> MANAGER;

    @CapabilityInject(IWorker.class)
    public static Capability<IWorker> WORKER;

}
