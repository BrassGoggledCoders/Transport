package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.mojang.datafixers.util.Either;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

public class SingleRoutingDeserializer<T> extends RoutingDeserializer {
    private final Function<T, Routing> constructor;
    private final Class<T> clazz;

    public SingleRoutingDeserializer(Class<T> clazz, Function<T, Routing> constructor) {
        this.constructor = constructor;
        this.clazz = clazz;
    }


    @Override
    @Nonnull
    public Either<String, Routing> deserialize(List<Object> inputs) {
        if (inputs.size() == 1) {
            Object input = inputs.get(0);
            if (clazz.isInstance(input)) {
                return Either.right(constructor.apply(clazz.cast(input)));
            } else {
                return Either.left("Found Type: " + input.getClass().getName() + " Expected: " + clazz.getName());
            }
        } else {
            return Either.left("Expected 1 input, Found " + inputs.size());
        }
    }
}
