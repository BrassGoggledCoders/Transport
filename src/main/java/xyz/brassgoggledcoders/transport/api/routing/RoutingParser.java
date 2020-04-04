package xyz.brassgoggledcoders.transport.api.routing;

import com.google.common.collect.Lists;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;

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
    public static Routing parse(@Nullable String route) {
        return parse(route, TransportAPI.getRoutingDeserializers());
    }

    public static Routing parse(@Nullable String route, Map<String, RoutingDeserializer> routingDeserializers) {
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
    public static Routing parseInstruction(String method, Iterator<String> routingMethodInputs,
                                           Map<String, RoutingDeserializer> routingDeserializers) {
        RoutingDeserializer routingDeserializer = routingDeserializers.get(method);
        if (routingDeserializer != null) {
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
                    Routing routing = parseInstruction(trimInstruction(routingInstructionInput),
                            routingMethodInputs, routingDeserializers);
                    if (routing != null) {
                        inputs.add(routing);
                    } else {
                        return null;
                    }
                }
            }
            return routingDeserializer.deserialize(inputs);
        }
        return null;
    }

    private static String trimInstruction(String name) {
        return name.toUpperCase(Locale.ENGLISH)
                .replace("{", "").trim();
    }

}
