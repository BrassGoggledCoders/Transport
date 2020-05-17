package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Blocks;

public class EmptyCargoModule extends CargoModule {
    public EmptyCargoModule() {
        super(() -> Blocks.AIR);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
