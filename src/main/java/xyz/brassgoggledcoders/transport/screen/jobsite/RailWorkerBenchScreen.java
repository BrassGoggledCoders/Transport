package xyz.brassgoggledcoders.transport.screen.jobsite;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.container.jobsite.RailWorkerBenchContainer;
import xyz.brassgoggledcoders.transport.recipe.jobsite.RailWorkerBenchRecipe;

public class RailWorkerBenchScreen extends SingleRecipeScreen<RailWorkerBenchContainer, RailWorkerBenchRecipe> {
    public RailWorkerBenchScreen(RailWorkerBenchContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }
}
