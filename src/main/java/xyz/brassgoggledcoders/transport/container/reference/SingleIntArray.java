package xyz.brassgoggledcoders.transport.container.reference;

import net.minecraft.util.IIntArray;

public class SingleIntArray implements IIntArray {
    private int value = 0;

    @Override
    public int get(int index) {
        return value;
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            this.value = value;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
