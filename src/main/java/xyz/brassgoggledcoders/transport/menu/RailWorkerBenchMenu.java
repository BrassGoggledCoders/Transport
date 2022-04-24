package xyz.brassgoggledcoders.transport.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import xyz.brassgoggledcoders.transport.content.TransportBlocks;
import xyz.brassgoggledcoders.transport.content.TransportContainers;
import xyz.brassgoggledcoders.transport.content.TransportRecipes;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipe;

public class RailWorkerBenchMenu extends JobSiteMenu<IRailWorkerBenchRecipe> {
    public RailWorkerBenchMenu(int menuId, Inventory inventory, ContainerLevelAccess levelAccess) {
        super(TransportContainers.RAIL_WORKER_BENCH.get(), menuId, inventory, levelAccess);
    }

    public RailWorkerBenchMenu(MenuType<RailWorkerBenchMenu> type, int windowId, Inventory inv) {
        super(type, windowId, inv, ContainerLevelAccess.NULL);
    }

    @Override
    protected Block getBlock() {
        return TransportBlocks.RAIL_WORKER_BENCH.get();
    }

    @Override
    protected RecipeType<IRailWorkerBenchRecipe> getRecipeType() {
        return TransportRecipes.RAIL_WORKER_BENCH_TYPE.get();
    }
}
