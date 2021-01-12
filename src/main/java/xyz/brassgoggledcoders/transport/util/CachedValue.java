package xyz.brassgoggledcoders.transport.util;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CachedValue<T> {
    private final Supplier<T> valueSupplier;
    private boolean supplied = false;
    private T value;

    public CachedValue(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    @Nullable
    public T getValue() {
        if (!this.supplied) {
            this.value = valueSupplier.get();
            this.supplied = true;
        }
        return this.value;
    }

    public void invalidate() {
        this.value = null;
        this.supplied = false;
    }
}
