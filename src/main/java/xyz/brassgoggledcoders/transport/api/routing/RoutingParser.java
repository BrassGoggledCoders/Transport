package xyz.brassgoggledcoders.transport.api.routing;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.routing.instruction.RoutingInstruction;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingInstructionDeserializer;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RoutingParser {
    private static final Predicate<String> ROUTING_START = Pattern.compile("ROUTING\\s*\\{\\s*").asPredicate();
    private static final Predicate<String> METHOD_END = Pattern.compile("\\s*}\\s*").asPredicate();
    private static final Predicate<String> METHOD_START = Pattern.compile("\\w+\\s*\\{\\s*").asPredicate();
    private static final Predicate<String> STRING_INPUT = Pattern.compile("\\s*\".+\"\\S*").asPredicate();
    private static final Predicate<String> NUMBER_INPUT = Pattern.compile("\\d+").asPredicate();

    @Nullable
    public static RoutingInstruction parse(@Nullable String route) {
        return parse(route, TransportAPI.getRoutingDeserializers());
    }

    public static RoutingInstruction parse(@Nullable String route, Map<String, RoutingInstructionDeserializer> routingDeserializers) {
        if (route != null) {
            List<String> routingInstructions = Arrays.asList(route.split("\\n"));
            if (routingInstructions.size() >= 4) {
                if (ROUTING_START.test(routingInstructions.get(0)) &&
                        METHOD_END.test(routingInstructions.get(routingInstructions.size() - 1))) {
                    String routingInstruction = routingInstructions.get(1);
                    if (METHOD_START.test(routingInstruction)) {
                        return parseInstruction(trimInstruction(routingInstruction),
                                routingInstructions.subList(2, routingInstructions.size() - 2).iterator(),
                                routingDeserializers);
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public static RoutingInstruction parseInstruction(String method, Iterator<String> routingMethodInputs,
                                                      Map<String, RoutingInstructionDeserializer> routingDeserializers) {
        RoutingInstructionDeserializer routingInstructionDeserializer = routingDeserializers.get(method);
        if (routingInstructionDeserializer != null) {
            List<Object> inputs = Lists.newArrayList();
            while (routingMethodInputs.hasNext()) {
                String routingInstructionInput = routingMethodInputs.next();
                if (METHOD_END.test(routingInstructionInput)) {
                    break;
                } else if (STRING_INPUT.test(routingInstructionInput)) {
                    inputs.add(routingInstructionInput.replace("\"", "").trim());
                } else if (NUMBER_INPUT.test(routingInstructionInput)) {
                    try {
                        inputs.add(Integer.parseInt(routingInstructionInput));
                    } catch (NumberFormatException exception) {
                        return null;
                    }
                } else if (METHOD_START.test(routingInstructionInput)) {
                    RoutingInstruction routingInstruction = parseInstruction(trimInstruction(routingInstructionInput),
                            routingMethodInputs, routingDeserializers);
                    if (routingInstruction != null) {
                        inputs.add(routingInstruction);
                    } else {
                        return null;
                    }
                }
            }
            return routingInstructionDeserializer.deserialize(inputs);
        }
        return null;
    }

    private static String trimInstruction(String name) {
        return name.toUpperCase(Locale.ENGLISH)
                .replace("{", "").trim();
    }

}
