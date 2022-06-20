package xyz.brassgoggledcoders.transport.littlelogistics.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.transport.api.TransportAPI;
import xyz.brassgoggledcoders.transport.api.shellcontent.ShellContent;
import xyz.brassgoggledcoders.transport.data.recipe.ShellItemRecipeBuilder;
import xyz.brassgoggledcoders.transport.littlelogistics.TransportLL;
import xyz.brassgoggledcoders.transport.littlelogistics.entity.ShellWagon;

public class TransportLLItems {
    public static RegistryObject<Item> SEATER_WAGON = RegistryObject.create(
            new ResourceLocation("littlelogistics:seater_car"),
            Registry.ITEM_REGISTRY,
            TransportLL.ID
    );

    public static ItemEntry<Item> SHELL_WAGON = TransportLL.getRegistrate()
            .object("shell_wagon")
            .item(properties -> TransportAPI.ITEM_HELPER.get()
                    .createShellMinecartItem((itemStack, level, pos) -> {
                        CompoundTag shellContentTag = itemStack.getTagElement("shellContent");
                        ShellContent shellContent = TransportAPI.SHELL_CONTENT_CREATOR.get().create(shellContentTag);

                        return new ShellWagon(TransportLLEntities.SHELL_WAGON.get(), level, pos, shellContent);
                    })
                    .apply(properties)
            )
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
