package xyz.brassgoggledcoders.transport.container.modular;

import xyz.brassgoggledcoders.transport.api.helper.ContainerHelper;
import xyz.brassgoggledcoders.transport.api.module.container.IModularContainer;
import xyz.brassgoggledcoders.transport.api.module.container.ModuleContainer;

public class BlankModuleContainer extends ModuleContainer {
    public BlankModuleContainer(IModularContainer modularContainer) {
        super(modularContainer);
    }

    @Override
    public void setup() {
        super.setup();
        this.addPlayerSlots();
    }
}
