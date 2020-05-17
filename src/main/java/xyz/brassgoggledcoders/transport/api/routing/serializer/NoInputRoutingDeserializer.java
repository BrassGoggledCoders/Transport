package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.mojang.datafixers.util.Either;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class NoInputRoutingDeserializer extends RoutingDeserializer {
    private final Supplier<Routing> routingInstructionSupplier;

    public NoInputRoutingDeserializer(Supplier<Routing> routingInstructionSupplier) {
        this.routingInstructionSupplier = routingInstructionSupplier;
    }

    @Override
    @Nonnull
    public Either<String, Routing> deserialize(List<Object> inputs) {
        if (inputs.isEmpty()) {
            return Either.right(routingInstructionSupplier.get());
        } else {
            return Either.left("Expected 0 values, Found " + inputs.size());
        }
    }
}
