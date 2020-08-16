package xyz.brassgoggledcoders.transport.registrate;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

import java.util.function.Supplier;

public class TransportRegistrate extends AbstractRegistrate<TransportRegistrate> {


    public static TransportRegistrate create(String modid) {
        Preconditions.checkNotNull(FMLJavaModLoadingContext.get(), "Registrate initialized too early!");
        return new TransportRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private TransportRegistrate(String modid) {
        super(modid);
    }

    public <T extends HullType> HullTypeBuilder<T, TransportRegistrate> hullType(Supplier<T> hullTypeCreator) {
        return this.entry((name, builderCallback) ->
                HullTypeBuilder.create(this, this, name, builderCallback, hullTypeCreator));
    }
}
