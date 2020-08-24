package xyz.brassgoggledcoders.transport.api.module;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class ModuleType extends ForgeRegistryEntry<ModuleType> {
    private final Function<ResourceLocation, Module<?>> loadValue;
    private final NonNullLazy<Collection<Module<?>>> getValues;

    private String translationKey;
    private ITextComponent name;

    public ModuleType(Function<ResourceLocation, Module<?>> loadValue, NonNullSupplier<Collection<Module<?>>> getValues) {
        this.loadValue = loadValue;
        this.getValues = NonNullLazy.of(getValues::get);
    }

    @Nonnull
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("module_type", this.getRegistryName());
        }
        return translationKey;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        if (name == null) {
            name = new TranslationTextComponent(this.getTranslationKey());
        }
        return name;
    }

    public String getName() {
        return Optional.ofNullable(this.getRegistryName())
                .map(ResourceLocation::getPath)
                .orElseThrow(() -> new IllegalStateException("No Registry Name Found"));
    }

    public Module<?> load(String registryName) {
        return load(new ResourceLocation(registryName));
    }

    public Module<?> load(ResourceLocation registryName) {
        return loadValue.apply(registryName);
    }

    public Collection<Module<?>> getValues() {
        return getValues.get();
    }
}
