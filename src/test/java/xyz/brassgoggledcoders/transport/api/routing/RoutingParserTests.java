package xyz.brassgoggledcoders.transport.api.routing;


import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;
import xyz.brassgoggledcoders.transport.api.routing.instruction.TrueInstruction;
import xyz.brassgoggledcoders.transport.api.routing.serializer.NoInputRoutingInstructionDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;
import xyz.brassgoggledcoders.transport.routing.instruction.NameTagRoutingDeserializer;

import java.util.Map;

public class RoutingParserTests {
    @Test
    @DisplayName("Null Input Returns Null")
    void testNullStringReturnsNull() {
        Assertions.assertNull(RoutingParser.parse(null));
    }

    @Test
    @DisplayName("No Routing Instruction Returns Null")
    void testNoRoutingInstructionsReturnsNull() {
        Assertions.assertNull(RoutingParser.parse("TRUE {\n}"));
    }

    @Test
    @DisplayName("Routing Instruction with no addition Instructions Returns Null")
    void testNoAdditionalInstructionsReturnsNull() {
        Assertions.assertNull(RoutingParser.parse("ROUTING {\n}"));
    }

    @Test
    @DisplayName("Routing Instruction with True Instruction should return")
    void testRoutingInstructionWithTrueInstruction() {
        RoutingInstruction routingInstruction = RoutingParser.parse("ROUTING {\nTRUE {\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertNotNull(routingInstruction);
    }

    @Test
    @DisplayName("Routing Instruction with NameTag Instruction should return")
    void testRoutingInstructionWithNameTagInstruction() {
        RoutingInstruction routingInstruction = RoutingParser.parse("ROUTING {\nNAME_TAG{\n\"HELLO\"\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertNotNull(routingInstruction);
    }

    private Map<String, RoutingDeserializer> createRoutingDeserializers() {
        Map<String, RoutingDeserializer> map = Maps.newHashMap();
        map.put("TRUE", new NoInputRoutingInstructionDeserializer(TrueInstruction::new));
        map.put("NAME_TAG", new NameTagRoutingDeserializer());
        return map;
    }
}
