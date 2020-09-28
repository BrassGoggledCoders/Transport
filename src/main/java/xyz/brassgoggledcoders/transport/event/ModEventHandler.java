package xyz.brassgoggledcoders.transport.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateRegistry;
import xyz.brassgoggledcoders.transport.api.transfer.EnergyTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.FluidTransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ITransferor;
import xyz.brassgoggledcoders.transport.api.transfer.ItemTransferor;
import xyz.brassgoggledcoders.transport.predicate.NamePredicate;
import xyz.brassgoggledcoders.transport.predicate.TimePredicate;

import java.util.function.Predicate;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Transport.ID, bus = Bus.MOD)
public class ModEventHandler {
    private final static String TRANSFERORS = "transferors";
    private final static String ENTITY_PREDICATES = "entity_predicates";
    private final static String STRING_PREDICATES = "string_predicates";

    @SubscribeEvent
    public static void imcEnqueue(InterModEnqueueEvent enqueueEvent) {
        //region transferors
        InterModComms.sendTo(Transport.ID, TRANSFERORS, ItemTransferor::new);
        InterModComms.sendTo(Transport.ID, TRANSFERORS, FluidTransferor::new);
        InterModComms.sendTo(Transport.ID, TRANSFERORS, EnergyTransferor::new);
        //endregion

        //region entity predicates
        sendEntityPredicate("ROUTING", PredicateParser::getNextEntityPredicate);
        sendEntityPredicate("TRUE", parser -> entity -> true);
        sendEntityPredicate("FALSE", parser -> entity -> false);
        sendEntityPredicate("NAME", NamePredicate::create);
        sendEntityPredicate("NOT", parser -> parser.getNextEntityPredicate().negate());
        sendEntityPredicate("POWERED", parser -> entity -> entity instanceof AbstractMinecartEntity &&
                ((AbstractMinecartEntity) entity).isPoweredCart());
        sendEntityPredicate("TIME", TimePredicate::create);
        sendEntityPredicate("AND", parse -> {
            Predicate<Entity> predicate = parse.getNextEntityPredicate();
            while (parse.hasNextPredicate()) {
                predicate = predicate.and(parse.getNextEntityPredicate());
            }
            return predicate;
        });
        sendEntityPredicate("OR", parse -> {
            Predicate<Entity> predicate = parse.getNextEntityPredicate();
            while (parse.hasNextPredicate()) {
                predicate = predicate.or(parse.getNextEntityPredicate());
            }
            return predicate;
        });
        //endregion

        //region string predicate
    }

    @SubscribeEvent
    public static void imcProcess(InterModProcessEvent processEvent) {
        processEvent.getIMCStream(TRANSFERORS::equalsIgnoreCase)
                .map(InterModComms.IMCMessage::<ITransferor<?>>getMessageSupplier)
                .map(Supplier::get)
                .forEach(TransportAPI::registerTransferor);

        processEvent.getIMCStream(ENTITY_PREDICATES::equalsIgnoreCase)
                .map(InterModComms.IMCMessage::<Pair<String, ThrowingFunction<PredicateParser, Predicate<Entity>,
                        PredicateParserException>>>getMessageSupplier)
                .map(Supplier::get)
                .forEach(pair -> PredicateRegistry.addEntityPredicateCreator(pair.getLeft(), pair.getRight()));

        processEvent.getIMCStream(STRING_PREDICATES::equalsIgnoreCase)
                .map(InterModComms.IMCMessage::<Pair<String, ThrowingFunction<PredicateParser, Predicate<String>,
                        PredicateParserException>>>getMessageSupplier)
                .map(Supplier::get)
                .forEach(pair -> PredicateRegistry.addStringPredicateCreator(pair.getLeft(), pair.getRight()));
    }

    public static void sendEntityPredicate(String name, ThrowingFunction<PredicateParser, Predicate<Entity>,
            PredicateParserException> parsing) {
        InterModComms.sendTo(Transport.ID, ENTITY_PREDICATES, () -> Pair.of(name, parsing));
    }

    public static void sendStringPredicate(String name, ThrowingFunction<PredicateParser, Predicate<String>,
            PredicateParserException> parsing) {
        InterModComms.sendTo(Transport.ID, STRING_PREDICATES, () -> Pair.of(name, parsing));
    }
}
