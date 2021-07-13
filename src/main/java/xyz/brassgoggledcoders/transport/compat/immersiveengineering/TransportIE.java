package xyz.brassgoggledcoders.transport.compat.immersiveengineering;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.cargo.CargoModule;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.cargoinstance.GenericChestCargoInstance;
import xyz.brassgoggledcoders.transport.cargoinstance.capability.EnergyCargoModuleInstance;
import xyz.brassgoggledcoders.transport.content.TransportEntities;
import xyz.brassgoggledcoders.transport.content.TransportItemTags;
import xyz.brassgoggledcoders.transport.item.HulledBoatItem;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrateRecipes;

public class TransportIE {

    public static RegistryEntry<HullType> TREATED_WOOD_HULL = Transport.getRegistrate()
            .object("treated_wood")
            .hullType(itemSupplier -> new HullType(itemSupplier::get, Transport.rl("treated_wood_boat.png")))
            .lang("Treated Wood Hull")
            .defaultRecipe(TransportEntities.MODULAR_BOAT_ITEM::get)
            .item("boat", HulledBoatItem::new, itemBuilder -> itemBuilder
                    .tag(TransportItemTags.HULLS_BOAT)
                    .model((context, modelProvider) -> modelProvider.generated(context))
                    .recipe(TransportRegistrateRecipes.vehicleShape("forge:treated_wood")))
            .register();

    public static final RegistryEntry<CargoModule> LV_CAPACITOR = createCapacitor("lv", 100000);
    public static final RegistryEntry<CargoModule> MV_CAPACITOR = createCapacitor("mv", 1000000);
    public static final RegistryEntry<CargoModule> HV_CAPACITOR = createCapacitor("hv", 4000000);

    public static void setup() {
        if (ModList.get().isLoaded("immersiveengineering")) {
            IEDependentSetup.setup();
        }
    }

    public static RegistryEntry<CargoModule> createCapacitor(String name, int amount) {
        return Transport.getRegistrate()
                .object("immersiveengineering/capacitor_" + name)
                .cargoModule(() -> CargoModule.fromBlockName(
                        new ResourceLocation("immersiveengineering", "capacitor_" + name),
                        (cargoModule, iModularEntity) -> new EnergyCargoModuleInstance(cargoModule, iModularEntity, amount),
                        true
                ))
                .register();
    }
}
