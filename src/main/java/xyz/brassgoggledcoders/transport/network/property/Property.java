package xyz.brassgoggledcoders.transport.network.property;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Property<T> {
    private final PropertyType<T> propertyType;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    private T currentValue;
    private T lastKnownValue;

    public Property(PropertyType<T> propertyType) {
        this.propertyType = propertyType;
        this.getter = () -> currentValue;
        this.setter = value -> currentValue = value;
    }

    public Property(PropertyType<T> propertyType, Supplier<T> getter, Consumer<T> setter) {
        this.propertyType = propertyType;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean isDirty() {
        T value = this.getter.get();
        boolean dirty = !propertyType.getEquals().test(value, lastKnownValue);
        this.lastKnownValue = value;
        return dirty;
    }

    @Nullable
    public T get() {
        return getter.get();
    }

    public void set(T value) {
        this.setter.accept(value);
    }

    public PropertyType<T> getPropertyType() {
        return this.propertyType;
    }
}
