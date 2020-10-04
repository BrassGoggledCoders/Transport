package xyz.brassgoggledcoders.transport.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.GenericEvent;
import xyz.brassgoggledcoders.transport.api.manager.IWorker;

import javax.annotation.Nonnull;

public class GetWorkerRepresentativeEvent<T> extends GenericEvent<T> {
    private final T task;
    private final IWorker worker;

    private ItemStack representative = ItemStack.EMPTY;

    public GetWorkerRepresentativeEvent(T task, IWorker worker) {
        this.task = task;
        this.worker = worker;
    }

    public T getTask() {
        return task;
    }

    public IWorker getWorker() {
        return worker;
    }

    public void setRepresentative(@Nonnull ItemStack representative) {
        this.representative = representative;
    }

    @Nonnull
    public ItemStack getRepresentative() {
        return this.representative;
    }
}
