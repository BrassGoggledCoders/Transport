package xyz.brassgoggledcoders.transport.content.compat;

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

@SuppressWarnings("unused")
public class QuarkCargoModules {
    private static final DeferredRegister<CargoModule> CARGO = DeferredRegister.create(CargoModule.class, Transport.ID);

    public static final RegistryObject<CargoModule> OAK_CHEST = createChestModule("oak_chest");
    public static final RegistryObject<CargoModule> SPRUCE_CHEST = createChestModule("spruce_chest");

    public static void register(IEventBus eventBus) {
        CARGO.register(eventBus);
    }

    private static RegistryObject<CargoModule> createChestModule(String path) {
        RegistryObject<Block> registryObject = RegistryObject.of(new ResourceLocation("quark", path),
                ForgeRegistries.BLOCKS);

        return CARGO.register("quark/" + path, () -> new CargoModule(() -> registryObject.orElse(Blocks.AIR),
                GenericChestCargoInstance::new));
    }
}
