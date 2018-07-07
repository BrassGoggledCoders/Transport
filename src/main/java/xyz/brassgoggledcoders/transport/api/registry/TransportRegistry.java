package xyz.brassgoggledcoders.transport.api.registry;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public abstract class TransportRegistry<T extends ITransportRegistryItem> {
    private Map<ResourceLocation, T> entries;

    public TransportRegistry() {
        entries = Maps.newHashMap();
    }

    public void addEntry(T entry) {
        this.entries.put(entry.getRegistryName(), entry);
    }

    @SafeVarargs
    public final void addEntries(T... entries) {
        for (T entry : entries) {
            addEntry(entry);
        }
    }

    public T getEntry(ResourceLocation resourceLocation) {
        return this.entries.getOrDefault(resourceLocation, getEmpty());
    }

    public abstract T getEmpty();
}
