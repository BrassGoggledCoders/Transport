package xyz.brassgoggledcoders.transport.api.routing;


import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.api.routing.instruction.TrueRouting;
import xyz.brassgoggledcoders.transport.api.routing.serializer.NoInputRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;

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
        Routing routing = RoutingParser.parse("ROUTING {\nTRUE {\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertNotNull(routing);
    }

    @Test
    @DisplayName("Routing Instruction with NameTag Instruction should return")
    void testRoutingInstructionWithNameTagInstruction() {
        Routing routing = RoutingParser.parse("ROUTING {\nNAME_TAG{\n\"HELLO\"\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertNotNull(routing);
    }

    private Map<String, RoutingDeserializer> createRoutingDeserializers() {
        Map<String, RoutingDeserializer> map = Maps.newHashMap();
        map.put("TRUE", new NoInputRoutingDeserializer(TrueRouting::new));
        map.put("NAME_TAG", new NameTagRoutingDeserializer());
        return map;
    }
}
