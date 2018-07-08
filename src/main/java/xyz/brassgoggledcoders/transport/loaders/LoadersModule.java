package xyz.brassgoggledcoders.transport.loaders;

import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;
import com.teamacronymcoders.base.registrysystem.config.ConfigRegistry;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.loaders.block.BlockFELoader;
import xyz.brassgoggledcoders.transport.loaders.block.BlockFluidLoader;
import xyz.brassgoggledcoders.transport.loaders.block.BlockItemLoader;

@Module(Transport.ID)
public class LoadersModule extends ModuleBase {
    @Override
    public String getName() {
        return "Loaders";
    }

    @Override
    public void registerBlocks(ConfigRegistry configRegistry, BlockRegistry blockRegistry) {
        super.registerBlocks(configRegistry, blockRegistry);
        blockRegistry.register(new BlockFELoader());
        blockRegistry.register(new BlockItemLoader());
        blockRegistry.register(new BlockFluidLoader());
    }
}
