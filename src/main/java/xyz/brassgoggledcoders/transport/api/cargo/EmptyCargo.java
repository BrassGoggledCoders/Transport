package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.block.Blocks;

public class EmptyCargo extends Cargo {
    public EmptyCargo() {
        super(() -> Blocks.AIR);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
