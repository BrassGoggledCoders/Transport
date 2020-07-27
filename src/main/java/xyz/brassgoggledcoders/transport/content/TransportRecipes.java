package xyz.brassgoggledcoders.transport.content;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.recipe.CargoRecipeSerializer;

@SuppressWarnings("unused")
public class TransportRecipes {
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, Transport.ID);

    public static final RegistryObject<CargoRecipeSerializer> CARGO_SERIALIZER = RECIPES.register("cargo",
            CargoRecipeSerializer::new);

    public static void register(IEventBus eventBus) {
        RECIPES.register(eventBus);
    }
}
