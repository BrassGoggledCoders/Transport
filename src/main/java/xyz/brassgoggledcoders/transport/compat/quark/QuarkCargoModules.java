package xyz.brassgoggledcoders.transport.compat.quark;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.cargoinstance.GenericChestCargoInstance;
import xyz.brassgoggledcoders.transport.compat.quark.TransportQuark;

@SuppressWarnings("unused")
public class QuarkCargoModules {
    public static final DeferredRegister<CargoModule> CARGO = DeferredRegister.create(CargoModule.class, Transport.ID);

    public static final RegistryObject<CargoModule> OAK_CHEST = createChestModule("oak_chest");
    public static final RegistryObject<CargoModule> SPRUCE_CHEST = createChestModule("spruce_chest");
    public static final RegistryObject<CargoModule> JUNGLE_CHEST = createChestModule("jungle_chest");
    public static final RegistryObject<CargoModule> ACACIA_CHEST = createChestModule("acacia_chest");
    public static final RegistryObject<CargoModule> BIRCH_CHEST = createChestModule("birch_chest");
    public static final RegistryObject<CargoModule> DARK_OAK_CHEST = createChestModule("dark_oak_chest");
    public static final RegistryObject<CargoModule> CRIMSON_CHEST = createChestModule("crimson_chest");
    public static final RegistryObject<CargoModule> WARPED_CHEST = createChestModule("warped_chest");
    public static final RegistryObject<CargoModule> NETHER_BRICK_CHEST = createChestModule("nether_brick_chest");
    public static final RegistryObject<CargoModule> PURPUR_CHEST = createChestModule("purpur_chest");
    public static final RegistryObject<CargoModule> PRISMARINE_CHEST = createChestModule("prismarine_chest");
    public static final RegistryObject<CargoModule> MUSHROOM_CHEST = createChestModule("mushroom_chest");

    public static void register(IEventBus eventBus) {
        CARGO.register(eventBus);
    }

    private static RegistryObject<CargoModule> createChestModule(String path) {
        RegistryObject<Block> registryObject = RegistryObject.of(new ResourceLocation("quark", path),
                ForgeRegistries.BLOCKS);

        return CARGO.register("quark/" + path, () -> new CargoModule(() -> registryObject.orElse(Blocks.AIR),
                GenericChestCargoInstance::new, true));
    }
}
