package xyz.brassgoggledcoders.transport.api.module.container;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import xyz.brassgoggledcoders.transport.api.module.screen.IModularScreen;
import xyz.brassgoggledcoders.transport.api.module.screen.ModuleScreen;

import java.util.function.BiFunction;

public class ModuleTab<T extends ModuleContainer> {
    private final ITextComponent displayName;
    private final NonNullLazy<ItemStack> displayStack;
    private final NonNullFunction<IModularContainer, T> moduleContainerCreator;
    private final NonNullSupplier<BiFunction<IModularScreen, T, ? extends ModuleScreen<T>>> moduleScreenCreator;

    public ModuleTab(
            ITextComponent displayName,
            NonNullSupplier<ItemStack> displayStack,
            NonNullFunction<IModularContainer, T> moduleContainerCreator,
            NonNullSupplier<BiFunction<IModularScreen, T, ? extends ModuleScreen<T>>> moduleScreenCreator
    ) {
        this.displayName = displayName;
        this.displayStack = NonNullLazy.of(displayStack);
        this.moduleContainerCreator = moduleContainerCreator;
        this.moduleScreenCreator = moduleScreenCreator;
    }

    public ITextComponent getDisplayName() {
        return displayName;
    }

    public ItemStack getDisplayStack() {
        return displayStack.get();
    }

    public NonNullFunction<IModularContainer, T> getModuleContainerCreator() {
        return moduleContainerCreator;
    }

    public NonNullSupplier<BiFunction<IModularScreen, T, ? extends ModuleScreen<T>>> getModuleScreenCreator() {
        return moduleScreenCreator;
    }
}
