package xyz.brassgoggledcoders.transport.api.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModuleItem<M extends Module<M>> extends Item {
    private final Supplier<M> module;

    public ModuleItem(Supplier<M> module, Properties properties) {
        super(properties);
        this.module = module;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return this.module.get().getDisplayName();
    }

    public M getModule() {
        return this.module.get();
    }

    public void onModuleSet(ItemStack itemStack) {
        itemStack.shrink(1);
    }
}
