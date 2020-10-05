package xyz.brassgoggledcoders.transport.screen.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class EnumButton<E extends Enum<E> & INameable> extends Button {
    private final Function<E, ITextComponent> getDisplayName;
    private final E[] values;
    private E currentValue;

    public EnumButton(E[] values, E startingValue, Function<E, ITextComponent> getDisplayName, int x, int y, int width,
                      int height) {
        super(x, y, width, height, getDisplayName.apply(startingValue), (button) -> {
        });
        this.getDisplayName = getDisplayName;
        this.values = values;
        this.currentValue = startingValue;
    }

    public void onPress() {
        int id = this.currentValue.ordinal();
        id++;
        if (id >= this.values.length) {
            id = 0;
        }
        this.currentValue = values[id];
        this.setMessage(this.getDisplayName.apply(this.currentValue));
    }
}
