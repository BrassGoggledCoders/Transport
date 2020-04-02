package xyz.brassgoggledcoders.transport.api;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import xyz.brassgoggledcoders.transport.api.cargo.Cargo;
import xyz.brassgoggledcoders.transport.api.engine.Engine;
import xyz.brassgoggledcoders.transport.api.module.ModuleType;
import xyz.brassgoggledcoders.transport.api.pointmachine.IPointMachineBehavior;

import java.util.Map;
import java.util.Objects;

public class TransportAPI {
    public static final Map<Block, IPointMachineBehavior> POINT_MACHINE_BEHAVIORS = Maps.newHashMap();

    public static Lazy<ForgeRegistry<Cargo>> CARGO = Lazy.of(() -> (ForgeRegistry<Cargo>)RegistryManager.ACTIVE.getRegistry(Cargo.class));
    public static Lazy<IForgeRegistry<Engine>> ENGINES = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(Engine.class));
    public static Lazy<IForgeRegistry<ModuleType<?>>> MODULE_TYPE = Lazy.of(() -> RegistryManager.ACTIVE.getRegistry(ModuleType.class));

    public static Cargo getCargo(String name) {
        return getCargo(new ResourceLocation(name));
    }

    public static Cargo getCargo(ResourceLocation name) {
        return CARGO.get().getValue(name);
    }

    public static Engine getEngine(String name) {
        return getEngine(new ResourceLocation(name));
    }

    public static Engine getEngine(ResourceLocation name) {
        return ENGINES.get().getValue(name);
    }

    public static ModuleType<?> getModuleType(String name) {
        return getModuleType(new ResourceLocation(name));
    }

    public static ModuleType<?> getModuleType(ResourceLocation resourceLocation) {
        return MODULE_TYPE.get().getValue(resourceLocation);
    }
}
