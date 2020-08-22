package xyz.brassgoggledcoders.transport.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.engine.EngineModule;
import xyz.brassgoggledcoders.transport.engine.BoosterEngineModuleInstance;
import xyz.brassgoggledcoders.transport.engine.CreativeEngineModuleInstance;
import xyz.brassgoggledcoders.transport.engine.SolidFuelEngineModuleInstance;

@SuppressWarnings("unused")
public class TransportEngineModules {
    public static RegistryEntry<EngineModule> CREATIVE = Transport.getRegistrate()
            .object("creative")
            .engineModule(CreativeEngineModuleInstance::new)
            .lang("Creative Engine")
            .item("creative_engine")
            .group(Transport::getItemGroup)
            .build()
            .register();

    public static RegistryEntry<EngineModule> SOLID_FUEL = Transport.getRegistrate()
            .object("solid_fuel")
            .engineModule(SolidFuelEngineModuleInstance::new)
            .lang("Solid Fuel Engine")
            .item("solid_fuel_engine")
            .group(Transport::getItemGroup)
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine("F")
                    .patternLine("F")
                    .key('F', Items.FURNACE)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Items.FURNACE))
                    .build(provider)
            )
            .build()
            .register();

    public static RegistryEntry<EngineModule> BOOSTER = Transport.getRegistrate()
            .object("booster")
            .engineModule(BoosterEngineModuleInstance::new)
            .item("booster_engine")
            .group(Transport::getItemGroup)
            .recipe((context, provider) -> ShapedRecipeBuilder.shapedRecipe(context.get())
                    .patternLine("G G")
                    .patternLine("RGR")
                    .patternLine("G G")
                    .key('G', Tags.Items.INGOTS_GOLD)
                    .key('R', Tags.Items.DUSTS_REDSTONE)
                    .addCriterion("has_item", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_GOLD))
                    .build(provider)
            )
            .build()
            .register();

    public static void setup() {
    }
}
