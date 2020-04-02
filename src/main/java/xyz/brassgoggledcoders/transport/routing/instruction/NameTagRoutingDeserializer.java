package xyz.brassgoggledcoders.transport.routing.instruction;

import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;

import java.util.List;

public class NameTagRoutingDeserializer extends RoutingDeserializer {
    @Override
    public RoutingInstruction deserialize(List<Object> inputs) {
        return inputs.size() == 1 && inputs.get(0) instanceof String ?
                new NameTagRoutingInstruction((String) inputs.get(0)) : null;
    }
}
