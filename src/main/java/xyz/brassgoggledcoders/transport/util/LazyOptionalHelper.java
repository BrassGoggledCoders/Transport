package xyz.brassgoggledcoders.transport.util;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class LazyOptionalHelper {
    public static <T> LazyOptional<T> getOrElse(LazyOptional<T> primary, NonNullSupplier<LazyOptional<T>> orElse) {
        if (primary.isPresent()) {
            return primary;
        } else {
            return orElse.get();
        }
    }
}
