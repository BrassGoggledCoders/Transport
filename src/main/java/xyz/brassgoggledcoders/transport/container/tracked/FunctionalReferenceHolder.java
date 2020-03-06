package xyz.brassgoggledcoders.transport.container.tracked;

import net.minecraft.util.IntReferenceHolder;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class FunctionalReferenceHolder extends IntReferenceHolder {
    private final IntSupplier supplier;
    private final IntConsumer consumer;

    public FunctionalReferenceHolder(IntSupplier supplier, IntConsumer consumer) {
        this.supplier = supplier;
        this.consumer = consumer;
    }

    @Override
    public int get() {
        return supplier.getAsInt();
    }

    @Override
    public void set(int value) {
        consumer.accept(value);
    }
}
