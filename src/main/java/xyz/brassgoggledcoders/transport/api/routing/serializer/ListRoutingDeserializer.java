package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import java.util.List;
import java.util.function.Function;

public class ListRoutingDeserializer<T> extends RoutingDeserializer {
    private final Function<List<T>, Routing> constructor;

    public ListRoutingDeserializer(Function<List<T>, Routing> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Routing deserialize(List<Object> inputs) {
        List<T> values = Lists.newArrayList();
        for (Object input: inputs) {
            try {
                //noinspection unchecked
                values.add((T)input);
            } catch (ClassCastException exception) {
                //Do nothing, wrong value type found.
            }
        }

        return constructor.apply(values);
    }
}
