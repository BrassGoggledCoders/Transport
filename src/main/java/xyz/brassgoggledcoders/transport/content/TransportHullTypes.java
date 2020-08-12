package xyz.brassgoggledcoders.transport.content;

import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.transport.Transport;
import xyz.brassgoggledcoders.transport.api.entity.HullType;
import xyz.brassgoggledcoders.transport.hulltype.VanillaBoatHullType;

public class TransportHullTypes {
    private static final DeferredRegister<HullType> HULL_TYPE = DeferredRegister.create(HullType.class, Transport.ID);

    public static final RegistryObject<HullType> OAK_BOAT = HULL_TYPE.register("oak_boat", () ->
            new VanillaBoatHullType(Items.OAK_BOAT, BoatEntity.Type.OAK));

    public static void register(IEventBus modEventBus) {
        HULL_TYPE.register(modEventBus);
    }
}
