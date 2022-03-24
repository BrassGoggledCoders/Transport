package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.ShellItemRecipeSerializer;

public class TransportRecipes {

    public static final RegistryEntry<ShellItemRecipeSerializer> SHELL_ITEMS = Transport.getRegistrate()
            .object("shell_items")
            .simple(RecipeSerializer.class, ShellItemRecipeSerializer::new);

    public static void setup() {

    }
}
