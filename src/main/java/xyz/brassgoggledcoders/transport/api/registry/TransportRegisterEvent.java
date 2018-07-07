package xyz.brassgoggledcoders.transport.api.registry;

import net.minecraftforge.fml.common.eventhandler.GenericEvent;

public class TransportRegisterEvent<T extends TransportRegistry> extends GenericEvent<T> {
    private T registry;

    public TransportRegisterEvent(T registry, Class<T> type) {
        super(type);
        this.registry = registry;
    }

    public T getRegistry() {
        return registry;
    }
}
