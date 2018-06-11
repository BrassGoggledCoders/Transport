package xyz.brassgoggledcoders.transport.rails;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.rails.block.BlockRailDiamondCrossing;
import xyz.brassgoggledcoders.transport.rails.block.BlockRailHolding;

@Module(Transport.ID)
public class RailsModule extends ModuleBase {
    @Override
    public String getName() {
        return "Rails";
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
