package xyz.brassgoggledcoders.transportlittlelogistics;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.transportlittlelogistics.content.TransportLittleLogisticsEntities;
import xyz.brassgoggledcoders.transportlittlelogistics.content.TransportLittleLogisticsItems;

import java.util.function.Supplier;

@Mod(TransportLittleLogistics.ID)
public class TransportLittleLogistics {
    public static final String ID = "transport_little_logistics";

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID));

    public TransportLittleLogistics() {
        TransportLittleLogisticsEntities.setup();
        TransportLittleLogisticsItems.setup();
    }
    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
