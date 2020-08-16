package xyz.brassgoggledcoders.transport.registrate;

import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.ArrayList;
import java.util.List;

public class TransportRegistration<R extends IForgeRegistryEntry<R>, T extends R> {
    private final ResourceLocation name;
    private final Class<? super R> type;
    private final LazyValue<? extends T> creator;
    private final NonNullLazy<RegistryEntry<T>> delegate;

    private final List<NonNullConsumer<? super T>> callbacks = new ArrayList<>();

    public TransportRegistration(ResourceLocation name, Class<? super R> type, NonNullSupplier<? extends T> creator, NonNullFunction<RegistryObject<T>, ? extends RegistryEntry<T>> entryFactory) {
        this.name = name;
        this.type = type;
        this.creator =  new LazyValue<>(creator);
        this.delegate = NonNullLazy.of(() -> entryFactory.apply(RegistryObject.of(name, RegistryManager.ACTIVE.<R>getRegistry(type))));
    }

    public void register(IForgeRegistry<R> registry) {
        T entry = creator.getValue();
        registry.register(entry.setRegistryName(name));
        delegate.get().updateReference(registry);
        callbacks.forEach(c -> c.accept(entry));
        callbacks.clear();
    }

    public void addRegisterCallback(NonNullConsumer<? super T> callback) {
        Preconditions.checkNotNull(callback, "Callback must not be null");
        callbacks.add(callback);
    }
}