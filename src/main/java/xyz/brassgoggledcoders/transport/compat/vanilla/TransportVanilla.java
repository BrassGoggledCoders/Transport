package xyz.brassgoggledcoders.transport.compat.vanilla;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.CakeCargoModuleInstance;

@SuppressWarnings("unused")
public class TransportVanilla {

    public static final RegistryEntry<CargoModule> SLIME = Transport.getRegistrate()
            .object("slime")
            .cargoModule(() -> Blocks.SLIME_BLOCK, CargoModuleInstance::new, true)
            .register();

    public static final RegistryEntry<CargoModule> CAKE = Transport.getRegistrate()
            .object("cake")
            .cargoModule(() -> Blocks.CAKE, CakeCargoModuleInstance::new, true)
            .register();

    public static void setup() {
    }
}
