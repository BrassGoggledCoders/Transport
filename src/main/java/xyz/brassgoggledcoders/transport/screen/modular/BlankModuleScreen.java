package xyz.brassgoggledcoders.transport.screen.modular;

import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;
import xyz.brassgoggledcoders.transport.container.modular.BlankModuleContainer;

public class BlankModuleScreen extends ModuleScreen<BlankModuleContainer> {
    private static final ResourceLocation BACKGROUND = Transport.rl("textures/screen/blank_with_player.png");

    public BlankModuleScreen(IModularScreen modularScreen, BlankModuleContainer moduleContainer) {
        super(modularScreen, moduleContainer);
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return BACKGROUND;
    }
}
