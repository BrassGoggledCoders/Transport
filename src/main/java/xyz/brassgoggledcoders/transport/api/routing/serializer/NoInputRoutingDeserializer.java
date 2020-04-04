package xyz.brassgoggledcoders.transport.api.routing.serializer;

import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import java.util.List;
import java.util.function.Supplier;

public class NoInputRoutingDeserializer extends RoutingDeserializer {
    private final Supplier<Routing> routingInstructionSupplier;

    public NoInputRoutingDeserializer(Supplier<Routing> routingInstructionSupplier) {
        this.routingInstructionSupplier = routingInstructionSupplier;
    }

    @Override
    public Routing deserialize(List<Object> inputs) {
        return routingInstructionSupplier.get();
    }
}
