package xyz.brassgoggledcoders.transport.littlelogistics.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.transport.recipe.shellitem.ShellItemRecipeBuilder;
import xyz.brassgoggledcoders.transport.littlelogistics.TransportLL;
import xyz.brassgoggledcoders.transport.littlelogistics.item.ShellWagonItem;

public class TransportLLItems {
    public static RegistryObject<Item> SEATER_WAGON = RegistryObject.create(
            new ResourceLocation("littlelogistics:seater_car"),
            Registry.ITEM_REGISTRY,
            TransportLL.ID
    );

    public static ItemEntry<ShellWagonItem> SHELL_WAGON = TransportLL.getRegistrate()
            .object("shell_wagon")
            .item(ShellWagonItem::new)
            .properties(properties -> properties.stacksTo(8))
            .model((context, provider) -> provider.generated(context, new ResourceLocation("littlelogistics", "item/seater_car")))
            .recipe((context, provider) -> ShellItemRecipeBuilder.of(context.get())
                    .withInput(Ingredient.of(SEATER_WAGON.get()))
                    .save(provider, context.getId())
            )
            .properties(properties -> properties.stacksTo(1))
            .register();


    public static void setup() {

    }
}
