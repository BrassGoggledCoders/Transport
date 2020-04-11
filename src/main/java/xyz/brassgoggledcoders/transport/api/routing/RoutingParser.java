package xyz.brassgoggledcoders.transport.api.routing;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.routing.instruction.Routing;
import xyz.brassgoggledcoders.transport.api.routing.serializer.RoutingDeserializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RoutingParser {
    private static final Predicate<String> ROUTING_START = Pattern.compile("ROUTING\\s*\\{\\s*").asPredicate();
    private static final Predicate<String> METHOD_END = Pattern.compile("\\s*}\\s*").asPredicate();
    private static final Predicate<String> METHOD_START = Pattern.compile("\\w+\\s*\\{\\s*").asPredicate();
    private static final Predicate<String> STRING_INPUT = Pattern.compile("\\s*\".+\"\\S*").asPredicate();
    private static final Predicate<String> NUMBER_INPUT = Pattern.compile("\\d+").asPredicate();

    @Nonnull
    public static Either<String, Routing> parse(@Nullable String route) {
        return parse(route, TransportAPI::getRoutingDeserializer);
    }

    @Nonnull
    public static Either<String, Routing> parse(@Nullable String route, Function<String, RoutingDeserializer> getDeserializer) {
        if (route != null && !route.isEmpty()) {
            List<String> routingInstructions = Arrays.asList(route.split("\\n"));
            if (routingInstructions.size() >= 4) {
                if (ROUTING_START.test(routingInstructions.get(0)) &&
                        METHOD_END.test(routingInstructions.get(routingInstructions.size() - 1))) {
                    String routingInstruction = routingInstructions.get(1);
                    if (METHOD_START.test(routingInstruction)) {
                        String method = trimInstruction(routingInstruction);
                        return parseRouting(method, routingInstructions.subList(2, routingInstructions.size() - 2)
                                .iterator(), getDeserializer)
                                .mapLeft(error -> method + " failed: " + error);
                    } else {
                        return Either.left("Failed to find valid Routing instruction after ROUTING");
                    }
                } else {
                    return Either.left("Invalid Routing formatting. Routing must start with ROUTING and end with }");
                }
            } else {
                return Either.left("Failed to find ROUTING or enough Routing instructions");
            }
        } else {
            return Either.left("Failed to find input to parse Routing from");
        }
    }

    @Nonnull
    public static Either<String, Routing> parseRouting(String method, Iterator<String> routingMethodInputs,
                                                       Function<String, RoutingDeserializer> getDeserializer) {
        RoutingDeserializer routingDeserializer = getDeserializer.apply(method);
        if (routingDeserializer != null) {
            List<Object> inputs = Lists.newArrayList();
            while (routingMethodInputs.hasNext()) {
                String routingInstructionInput = routingMethodInputs.next();
                if (routingInstructionInput != null && !routingInstructionInput.trim().isEmpty()) {
                    routingInstructionInput = routingInstructionInput.trim();
                    if (METHOD_END.test(routingInstructionInput)) {
                        break;
                    } else if (STRING_INPUT.test(routingInstructionInput)) {
                        inputs.add(routingInstructionInput.replace("\"", "").trim());
                    } else if (NUMBER_INPUT.test(routingInstructionInput)) {
                        try {
                            inputs.add(Integer.parseInt(routingInstructionInput.trim()));
                        } catch (NumberFormatException exception) {
                            return Either.left(routingInstructionInput + " is not a valid number");
                        }
                    } else if (METHOD_START.test(routingInstructionInput)) {
                        String newMethodName = trimInstruction(routingInstructionInput);
                        return parseRouting(newMethodName, routingMethodInputs, getDeserializer)
                                .mapLeft(error -> newMethodName + " failed: " + error);
                    } else {
                        return Either.left("Unable to parse value for Routing: " + routingInstructionInput);
                    }
                } else {
                    return Either.left("Found no value for Parsing");
                }

            }
            return routingDeserializer.deserialize(inputs);
        } else {
            return Either.left("Failed to find Routing for " + method);
        }
    }

    private static String trimInstruction(String name) {
        return name.toUpperCase(Locale.ENGLISH)
                .replace("{", "").trim();
    }

}
