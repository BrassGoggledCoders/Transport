package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

public class ListRoutingDeserializer<T> extends RoutingDeserializer {
    private final Function<List<T>, Routing> constructor;
    private final Class<T> clazz;

    public ListRoutingDeserializer(Class<T> clazz, Function<List<T>, Routing> constructor) {
        this.constructor = constructor;
        this.clazz = clazz;
    }

    @Override
    @Nonnull
    public Either<String, Routing> deserialize(List<Object> inputs) {
        List<T> values = Lists.newArrayList();
        for (Object input: inputs) {
            if (clazz.isInstance(input)) {
                values.add(clazz.cast(input));
            }
        }

        if (!values.isEmpty()) {
            return Either.right(constructor.apply(values));
        } else {
            return null;
        }
    }
}
