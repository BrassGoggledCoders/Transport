package xyz.brassgoggledcoders.transport.registrate;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.transport.api.entity.HullType;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class TransportRegistrate extends AbstractRegistrate<TransportRegistrate> {
    public static TransportRegistrate create(String modid) {
        Preconditions.checkNotNull(FMLJavaModLoadingContext.get(), "Registrate initialized too early!");
        return new TransportRegistrate(modid).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private TransportRegistrate(String modid) {
        super(modid);
    }

    public <T extends HullType, I extends Item> HullTypeBuilder<T, I, TransportRegistrate> hullType(Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.hullType(this, hullTypeCreator);
    }

    public <T extends HullType, I extends Item, P> HullTypeBuilder<T, I, P> hullType(P parent, Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.entry((name, builderCallback) ->
                HullTypeBuilder.create(this, parent, name, builderCallback, hullTypeCreator));
    }
}
