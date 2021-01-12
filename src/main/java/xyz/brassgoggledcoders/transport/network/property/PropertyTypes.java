package xyz.brassgoggledcoders.transport.network.property;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PropertyTypes {
    private static final List<PropertyType<?>> types = Lists.newArrayList();

    public static PropertyType<FluidStack> FLUID_STACK = addType("fluid_stack", FluidStack.class,
            PacketBuffer::readFluidStack, PacketBuffer::writeFluidStack, FluidStack::isFluidEqual);
    public static PropertyType<Boolean> BOOLEAN = addType("boolean", Boolean.class, PacketBuffer::readBoolean,
            PacketBuffer::writeBoolean);
    public static PropertyType<Integer> INTEGER = addType("integer", Integer.class, PacketBuffer::readInt,
            PacketBuffer::writeInt);
    public static PropertyType<Double> DOUBLE = addType("double", Double.class, PacketBuffer::readDouble,
            PacketBuffer::writeDouble);

    public static <T> PropertyType<T> addType(String name, Class<T> tClass, Function<PacketBuffer, T> reader,
                                              BiConsumer<PacketBuffer, T> writer) {
        return addType(new PropertyType<>(name, tClass, reader, writer));
    }

    public static <T> PropertyType<T> addType(String name, Class<T> tClass, Function<PacketBuffer, T> reader,
                                              BiConsumer<PacketBuffer, T> writer, BiPredicate<T, T> equals) {
        return addType(new PropertyType<>(name, tClass, reader, writer, equals));
    }

    public static <T> PropertyType<T> addType(PropertyType<T> type) {
        types.add(type);
        types.sort(PropertyType::compareTo);
        return type;
    }

    public static short getIndex(PropertyType<?> propertyType) {
        return (short) types.indexOf(propertyType);
    }

    public static PropertyType<?> getByIndex(short index) {
        return types.get(index);
    }
}
