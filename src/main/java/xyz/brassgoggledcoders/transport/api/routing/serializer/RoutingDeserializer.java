package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.mojang.datafixers.util.Either;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class RoutingDeserializer {
    @Nonnull
    public abstract Either<String, Routing> deserialize(List<Object> inputs);
}
