package xyz.brassgoggledcoders.transport.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;

public record RailPlaceResult(
        InteractionResult result,
        BlockPos position
) {

    public boolean consumesAction() {
        return result().consumesAction();
    }
}
