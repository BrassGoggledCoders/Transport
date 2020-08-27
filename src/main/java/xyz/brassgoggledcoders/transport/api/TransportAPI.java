package xyz.brassgoggledcoders.transport.api;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.connection.IConnectionChecker;
import xyz.brassgoggledcoders.transport.api.connection.NoConnectionChecker;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.api.entity.IModularEntity;
import xyz.brassgoggledcoders.transport.api.functional.ThrowingFunction;
import xyz.brassgoggledcoders.transport.api.module.Module;
import xyz.brassgoggledcoders.transport.api.module.ModuleSlot;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.network.INetworkHandler;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParser;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateParserException;
import xyz.brassgoggledcoders.transport.api.predicate.PredicateStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class TransportAPI {
    public static final Logger LOGGER = LogManager.getLogger("transport-api");

    @CapabilityInject(PredicateStorage.class)
    public static Capability<PredicateStorage> PREDICATE_STORAGE;

    @CapabilityInject(IModularEntity.class)
    public static Capability<IModularEntity> MODULAR_ENTITY;

    private static IConnectionChecker connectionChecker = new NoConnectionChecker();
    private static INetworkHandler networkHandler;

    private static final Map<Block, IPointMachineBehavior> POINT_MACHINE_BEHAVIORS = Maps.newHashMap();
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException>>
            ENTITY_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<String, ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException>>
            STRING_PREDICATE_CREATORS = Maps.newHashMap();
    private static final Map<Item, Module<?>> ITEM_TO_MODULE = Maps.newHashMap();

    public static Lazy<ForgeRegistry<CargoModule>> CARGO = Lazy.of(() -> (ForgeRegistry<CargoModule>) RegistryManager.ACTIVE.getRegistry(CargoModule.class));
    public static Lazy<IForgeRegistry<EngineModule>> ENGINES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(EngineModule.class));
    public static Lazy<IForgeRegistry<ModuleType>> MODULE_TYPE = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(ModuleType.class));
    public static Lazy<IForgeRegistry<ModuleSlot>> MODULE_SLOT = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(ModuleSlot.class));
    public static Lazy<IForgeRegistry<HullType>> HULL_TYPE = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(HullType.class));

    public static CargoModule getCargo(String name) {
        return getCargo(new ResourceLocation(name));
    }

    public static CargoModule getCargo(ResourceLocation name) {
        return CARGO.get().getValue(name);
    }

    public static Collection<CargoModule> getCargo() {
        return CARGO.get().getValues();
    }

    public static EngineModule getEngine(String name) {
        return getEngine(new ResourceLocation(name));
    }

    public static EngineModule getEngine(ResourceLocation name) {
        return ENGINES.get().getValue(name);
    }

    public static Collection<EngineModule> getEngines() {
        return ENGINES.get().getValues();
    }

    public static ModuleType getModuleType(String name) {
        return getModuleType(new ResourceLocation(name));
    }

    public static ModuleType getModuleType(ResourceLocation resourceLocation) {
        return MODULE_TYPE.get().getValue(resourceLocation);
    }

    public static void addEntityPredicateCreator(String name, ThrowingFunction<PredicateParser, Predicate<Entity>,
            PredicateParserException> entityPredicateCreator) {
        ENTITY_PREDICATE_CREATORS.put(name.toUpperCase(Locale.US), entityPredicateCreator);
    }

    public static ThrowingFunction<PredicateParser, Predicate<Entity>, PredicateParserException> getEntityPredicateCreator(String name) {
        return ENTITY_PREDICATE_CREATORS.get(name.toUpperCase(Locale.US));
    }

    public static void addStringPredicateCreator(String name, ThrowingFunction<PredicateParser, Predicate<String>,
            PredicateParserException> stringPredicateCreator) {
        STRING_PREDICATE_CREATORS.put(name.toUpperCase(Locale.US), stringPredicateCreator);
    }

    public static ThrowingFunction<PredicateParser, Predicate<String>, PredicateParserException> getStringPredicateCreator(String name) {
        return STRING_PREDICATE_CREATORS.get(name.toUpperCase(Locale.US));
    }


    @Nullable
    public static IPointMachineBehavior getPointMachineBehavior(Block block) {
        return POINT_MACHINE_BEHAVIORS.get(block);
    }

    public static void addPointMachineBehavior(Block block, IPointMachineBehavior pointMachineBehavior) {
        POINT_MACHINE_BEHAVIORS.put(block, pointMachineBehavior);
    }

    public static Map<Block, IPointMachineBehavior> getPointMachineBehaviors() {
        return POINT_MACHINE_BEHAVIORS;
    }

    @Nullable
    public static Module<?> getModuleFromItem(Item item) {
        return ITEM_TO_MODULE.get(item);
    }

    public static void generateItemToModuleMap() {
        for (ModuleType type : MODULE_TYPE.get().getValues()) {
            Collection<Module<?>> modules = type.getValues();
            for (Module<?> module : modules) {
                if (module.isActive()) {
                    Item item = module.asItem();
                    Module<?> originalModule = ITEM_TO_MODULE.get(item);
                    if (originalModule == null) {
                        ITEM_TO_MODULE.put(module.asItem(), module);
                    } else {
                        LOGGER.warn("Found multiple modules using same item: {}, new module: {}, old module: {}",
                                item.getRegistryName(), module.getRegistryName(), originalModule.getRegistryName());
                    }
                }
            }
        }
    }

    public static void setNetworkHandler(INetworkHandler newNetworkHandler) {
        if (networkHandler != null) {
            throw new IllegalStateException("Tried to start Network Handler again");
        } else {
            networkHandler = newNetworkHandler;
        }
    }

    public static INetworkHandler getNetworkHandler() {
        return Objects.requireNonNull(networkHandler, "Network Handler has not been set");
    }

    @Nonnull
    public static IConnectionChecker getConnectionChecker() {
        return connectionChecker;
    }

    public static void setConnectionChecker(@Nonnull IConnectionChecker connectionChecker) {
        TransportAPI.connectionChecker = connectionChecker;
    }

    public static ModuleSlot getModuleSlot(String moduleSlot) {
        return TransportAPI.getModuleSlot(new ResourceLocation(moduleSlot));
    }

    public static ModuleSlot getModuleSlot(ResourceLocation resourceLocation) {
        return TransportAPI.MODULE_SLOT.get().getValue(resourceLocation);
    }
}
