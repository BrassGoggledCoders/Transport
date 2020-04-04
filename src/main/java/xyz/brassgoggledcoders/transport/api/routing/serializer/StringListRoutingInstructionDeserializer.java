package xyz.brassgoggledcoders.transport.api.routing.serializer;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;

import java.util.List;
import java.util.function.Function;

public class StringListRoutingInstructionDeserializer extends RoutingInstructionDeserializer {
    private final Function<List<String>, RoutingInstruction> constructor;

    public StringListRoutingInstructionDeserializer(Function<List<String>, RoutingInstruction> constructor) {
        this.constructor = constructor;
    }

    @Override
    public RoutingInstruction deserialize(List<Object> inputs) {
        List<String> strings = Lists.newArrayList();
        for (Object input: inputs) {
            if (input instanceof String) {
                strings.add((String) input);
            } else if (input instanceof Number) {
                strings.add(input.toString());
            }
        }

        return constructor.apply(strings);
    }
}
