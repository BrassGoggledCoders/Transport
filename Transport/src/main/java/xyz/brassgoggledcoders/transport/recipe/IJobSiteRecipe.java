package xyz.brassgoggledcoders.transport.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;

public interface IJobSiteRecipe<T extends IJobSiteRecipe<T>> extends Recipe<Container> {
    boolean reduceContainer(Container container);

    Collection<T> getChildren();
}
