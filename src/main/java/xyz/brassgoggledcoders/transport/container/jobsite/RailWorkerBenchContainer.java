package xyz.brassgoggledcoders.transport.container.jobsite;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;

public class RailWorkerBenchContainer extends SingleRecipeContainer<RailWorkerBenchRecipe> {
    public RailWorkerBenchContainer(ContainerType<? extends RailWorkerBenchContainer> containerType, int windowId,
                                    PlayerInventory playerInventory) {
        super(containerType, windowId, playerInventory, IWorldPosCallable.DUMMY, TransportRecipes.RAIL_WORKER_BENCH_TYPE);
    }

    public RailWorkerBenchContainer(int windowId, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(TransportBlocks.RAIL_WORKER_BENCH_CONTAINER.get(), windowId, playerInventory, worldPosCallable,
                TransportRecipes.RAIL_WORKER_BENCH_TYPE);
    }

    @Override
    protected Block getBlock() {
        return TransportBlocks.RAIL_WORKER_BENCH.get();
    }
}
