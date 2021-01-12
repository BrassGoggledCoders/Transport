package xyz.brassgoggledcoders.transport.util;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;

import java.util.List;
import java.util.ListIterator;

public class BlockStateHelper {
    public static <T extends Comparable<T>> BlockState cyclePrevious(BlockState blockState, Property<T> property) {
        if (blockState.hasProperty(property)) {
            List<T> allowedValues = Lists.newArrayList(property.getAllowedValues());
            T currentValue = blockState.get(property);
            T cycled = cyclePrevious(allowedValues, currentValue);
            if (currentValue != cycled) {
                return blockState.with(property, cycled);
            }
        }
        return blockState;
    }

    private static <T extends Comparable<T>> T cyclePrevious(List<T> allowedValues, T currentValue) {
        ListIterator<T> values = allowedValues.listIterator(allowedValues.size());

        while (values.hasPrevious()) {
            T previousValue = values.previous();
            if (previousValue.equals(currentValue)) {
                if (values.hasPrevious()) {
                    return values.previous();
                } else {
                    return previousValue;
                }
            }
        }

        return allowedValues.listIterator(allowedValues.size()).previous();
    }
}
