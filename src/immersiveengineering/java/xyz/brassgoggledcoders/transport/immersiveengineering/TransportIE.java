package xyz.brassgoggledcoders.transport.immersiveengineering;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.registrate.TransportRegistrate;

@Mod(TransportIE.ID)
public class TransportIE {
    public static final String ID = "transport_ie";

    public static final RegistryObject<Item> TREATED_WOOD_PLANKS = RegistryObject.of(
            new ResourceLocation("immersiveengineering", "treated_wood_planks"), ForgeRegistries.ITEMS);

    public TransportIE() {
        TransportRegistrate.create(ID)
                .object("treated_wood")
                .hullType(() -> new HullType(TREATED_WOOD_PLANKS, () -> new ResourceLocation(TransportIE.ID,
                        "textures/entity/treated_wood_boat.png"))
                )
                .lang("Treated Wood");

    }

}
