package xyz.brassgoggledcoders.transport.api.routing;


import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.routing.instruction.TrueRouting;
import xyz.brassgoggledcoders.transport.api.routing.serializer.ListRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.NoInputRoutingDeserializer;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;
import xyz.brassgoggledcoders.transport.routing.instruction.NameRouting;

import java.util.Map;

public class RoutingParserTests {
    @Test
    @DisplayName("Null Input Returns Left")
    void testNullStringReturnsNull() {
        Assertions.assertTrue(RoutingParser.parse(null).left().isPresent());
    }

    @Test
    @DisplayName("No Routing Instruction Returns Left")
    void testNoRoutingInstructionsReturnsNull() {
        Assertions.assertTrue(RoutingParser.parse("TRUE {\n}").left().isPresent());
    }

    @Test
    @DisplayName("Routing Instruction with no addition Instructions Returns Left")
    void testNoAdditionalInstructionsReturnsNull() {
        Assertions.assertTrue(RoutingParser.parse("ROUTING {\n}").left().isPresent());
    }

    @Test
    @DisplayName("Routing Instruction with True Instruction should return")
    void testRoutingInstructionWithTrueInstruction() {
        Either<String, Routing> routing = RoutingParser.parse("ROUTING {\nTRUE {\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertTrue(routing.right().isPresent());
    }

    @Test
    @DisplayName("Routing Instruction with NameTag Instruction should return")
    void testRoutingInstructionWithNameTagInstruction() {
        Either<String, Routing> routing = RoutingParser.parse("ROUTING {\nNAME{\n\"HELLO\"\n}\n}",
                this.createRoutingDeserializers());
        Assertions.assertTrue(routing.right().isPresent());
    }

    private Map<String, RoutingDeserializer> createRoutingDeserializers() {
        Map<String, RoutingDeserializer> map = Maps.newHashMap();
        map.put("TRUE", new NoInputRoutingDeserializer(TrueRouting::new));
        map.put("NAME", new ListRoutingDeserializer<>(String.class, NameRouting::new));
        return map;
    }
}
