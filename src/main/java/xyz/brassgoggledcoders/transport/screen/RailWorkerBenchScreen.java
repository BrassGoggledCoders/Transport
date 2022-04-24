package xyz.brassgoggledcoders.transport.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import xyz.brassgoggledcoders.transport.menu.RailWorkerBenchMenu;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.IRailWorkerBenchRecipe;
import xyz.brassgoggledcoders.transport.recipe.railworkerbench.RailWorkerBenchRecipe;

public class RailWorkerBenchScreen extends JobSiteScreen<RailWorkerBenchMenu, IRailWorkerBenchRecipe> {
    public RailWorkerBenchScreen(RailWorkerBenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
}
