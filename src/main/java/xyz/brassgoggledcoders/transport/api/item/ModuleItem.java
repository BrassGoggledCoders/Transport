package xyz.brassgoggledcoders.transport.api.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.module.Module;

import javax.annotation.Nonnull;

public class ModuleItem<M extends Module<M>> extends Item {
    private final NonNullSupplier<M> module;

    public ModuleItem(NonNullSupplier<M> module, Properties properties) {
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
