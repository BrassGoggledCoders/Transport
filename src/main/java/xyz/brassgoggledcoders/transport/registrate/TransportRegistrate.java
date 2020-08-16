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

    @Override
    @Nonnull
    protected TransportRegistrate registerEventListeners(IEventBus bus) {
        EventDispatcher onRegister = new EventDispatcher(this::onRegister);
        EventDispatcher onRegisterLate = new EventDispatcher(this::onRegisterLate);
        bus.register(onRegister);
        bus.register(onRegisterLate);
        OneTimeEventReceiver.addListener(bus, FMLCommonSetupEvent.class, ($) -> {
            bus.unregister(onRegister);
            bus.unregister(onRegisterLate);
        });
        OneTimeEventReceiver.addListener(bus, GatherDataEvent.class, this::onData);

        return this.self();
    }

    public <T extends HullType, I extends Item> HullTypeBuilder<T, I, TransportRegistrate> hullType(Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.hullType(this, hullTypeCreator);
    }

    public <T extends HullType, I extends Item, P> HullTypeBuilder<T, I, P> hullType(P parent, Function<NonNullSupplier<I>, T> hullTypeCreator) {
        return this.entry((name, builderCallback) ->
                HullTypeBuilder.create(this, parent, name, builderCallback, hullTypeCreator));
    }
}
