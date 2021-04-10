package xyz.brassgoggledcoders.transport.compat.quark;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.cargoinstance.GenericChestCargoInstance;

@SuppressWarnings("unused")
public class TransportQuark {

    public static final RegistryEntry<CargoModule> OAK_CHEST = createChestModule("oak_chest");
    public static final RegistryEntry<CargoModule> SPRUCE_CHEST = createChestModule("spruce_chest");
    public static final RegistryEntry<CargoModule> JUNGLE_CHEST = createChestModule("jungle_chest");
    public static final RegistryEntry<CargoModule> ACACIA_CHEST = createChestModule("acacia_chest");
    public static final RegistryEntry<CargoModule> BIRCH_CHEST = createChestModule("birch_chest");
    public static final RegistryEntry<CargoModule> DARK_OAK_CHEST = createChestModule("dark_oak_chest");
    public static final RegistryEntry<CargoModule> CRIMSON_CHEST = createChestModule("crimson_chest");
    public static final RegistryEntry<CargoModule> WARPED_CHEST = createChestModule("warped_chest");
    public static final RegistryEntry<CargoModule> NETHER_BRICK_CHEST = createChestModule("nether_brick_chest");
    public static final RegistryEntry<CargoModule> PURPUR_CHEST = createChestModule("purpur_chest");
    public static final RegistryEntry<CargoModule> PRISMARINE_CHEST = createChestModule("prismarine_chest");
    public static final RegistryEntry<CargoModule> MUSHROOM_CHEST = createChestModule("mushroom_chest");

    public static void setup() {
        if (ModList.get().isLoaded("quark")) {
            TransportAPI.setConnectionChecker(new QuarkConnectionChecker());
        }
    }

    private static RegistryEntry<CargoModule> createChestModule(String path) {
        RegistryObject<Block> registryObject = RegistryObject.of(new ResourceLocation("quark", path),
                ForgeRegistries.BLOCKS);

        return Transport.getRegistrate()
                .object("quark/" + path)
                .cargoModule(() -> new CargoModule(
                        () -> registryObject.orElse(Blocks.AIR),
                        GenericChestCargoInstance::new,
                        true
                ))
                .register();
    }
}
