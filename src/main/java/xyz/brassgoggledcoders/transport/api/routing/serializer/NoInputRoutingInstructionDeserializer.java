package xyz.brassgoggledcoders.transport.api.routing.serializer;

import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;

import java.util.List;
import java.util.function.Supplier;

public class NoInputRoutingInstructionDeserializer extends RoutingInstructionDeserializer {
    private final Supplier<RoutingInstruction> routingInstructionSupplier;

    public NoInputRoutingInstructionDeserializer(Supplier<RoutingInstruction> routingInstructionSupplier) {
        this.routingInstructionSupplier = routingInstructionSupplier;
    }

    @Override
    public RoutingInstruction deserialize(List<Object> inputs) {
        return routingInstructionSupplier.get();
    }
}
