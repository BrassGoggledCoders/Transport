package xyz.brassgoggledcoders.transport.tracks;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.tracks.block.BlockRailDiamondCrossing;
import xyz.brassgoggledcoders.transport.tracks.block.BlockRailHolding;
import xyz.brassgoggledcoders.transport.tracks.block.BlockRailSwitch;
import xyz.brassgoggledcoders.transport.tracks.block.BlockRailWye;

@Module(Transport.ID)
public class TracksModule extends ModuleBase {
    @Override
    public String getName() {
        return "Tracks";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry blockRegistry) {
        super.registerBlocks(configRegistry, blockRegistry);
        blockRegistry.register(new BlockRailHolding());
        blockRegistry.register(new BlockRailDiamondCrossing());
        //blockRegistry.register(new BlockRailSwitch());
        //blockRegistry.register(new BlockRailWye());
    }
}
