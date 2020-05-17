package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModuleType<T> extends ForgeRegistryEntry<ModuleType<?>> {
    private final Function<ResourceLocation, T> loadValue;
    private final Supplier<Collection<T>> getValues;

    private String translationKey;
    private ITextComponent name;

    public ModuleType(Function<ResourceLocation, T> loadValue, Supplier<Collection<T>> getValues) {
        this.loadValue = loadValue;
        this.getValues = getValues;
    }

    @Nonnull
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("component_type", this.getRegistryName());
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

    public T load(String registryName) {
        return load(new ResourceLocation(registryName));
    }

    public T load(ResourceLocation registryName) {
        return loadValue.apply(registryName);
    }

    public Collection<T> getValues() {
        return getValues.get();
    }
}
