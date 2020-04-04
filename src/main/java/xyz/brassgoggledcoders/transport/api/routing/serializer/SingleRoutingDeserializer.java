package xyz.brassgoggledcoders.transport.api.routing.serializer;

import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

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
    public Routing deserialize(List<Object> inputs) {
        if (!inputs.isEmpty()) {
            Object input = inputs.get(0);
            if (clazz.isInstance(input)) {
                return constructor.apply(clazz.cast(input));
            }
        }
        return null;
    }
}
