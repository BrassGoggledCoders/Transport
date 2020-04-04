package xyz.brassgoggledcoders.transport.api.routing.serializer;

import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;

import java.util.List;

public abstract class RoutingDeserializer {
    public abstract Routing deserialize(List<Object> inputs);
}
