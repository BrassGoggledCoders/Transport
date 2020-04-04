package xyz.brassgoggledcoders.transport.api.routing.serializer;

import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;

import java.util.List;

public abstract class RoutingInstructionDeserializer {
    public abstract RoutingInstruction deserialize(List<Object> inputs);
}