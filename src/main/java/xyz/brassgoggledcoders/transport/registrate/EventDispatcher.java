package xyz.brassgoggledcoders.transport.registrate;

import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

public class EventDispatcher {
    private final Consumer<RegistryEvent.Register<?>> eventConsumer;

    public EventDispatcher(Consumer<RegistryEvent.Register<?>> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @SubscribeEvent
    public void handleRegister(RegistryEvent.Register<?> registryEvent) {
        this.eventConsumer.accept(registryEvent);
    }
}
