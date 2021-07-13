package xyz.brassgoggledcoders.transport.compat.vanilla;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.CakeCargoModuleInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.GenericChestCargoInstance;
import xyz.brassgoggledcoders.transport.compat.vanilla.module.cargo.NoteBlockCargoModuleInstance;
import xyz.brassgoggledcoders.transport.compat.vanilla.module.cargo.SpawnerCargoModuleInstance;
import xyz.brassgoggledcoders.transport.compat.vanilla.renderer.SpawnerCargoModuleRenderer;

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

    public static final RegistryEntry<CargoModule> CHEST = Transport.getRegistrate()
            .object("chest")
            .cargoModule(() -> Blocks.CHEST, GenericChestCargoInstance::new, true)
            .register();

    public static final RegistryEntry<CargoModule> NOTE_BLOCK = Transport.getRegistrate()
            .object("note_block")
            .cargoModule(() -> Blocks.NOTE_BLOCK, NoteBlockCargoModuleInstance::new, true)
            .register();

    public static final RegistryEntry<CargoModule> SPAWNER = Transport.getRegistrate()
            .object("spawner")
            .cargoModule(() -> Blocks.SPAWNER, SpawnerCargoModuleInstance::new, true)
            .renderer(() -> SpawnerCargoModuleRenderer::new)
            .register();

    public static void setup() {
    }
}
