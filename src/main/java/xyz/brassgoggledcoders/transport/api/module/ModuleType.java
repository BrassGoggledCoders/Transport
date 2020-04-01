package xyz.brassgoggledcoders.transport.api.module;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Function;

public class ModuleType<T> extends ForgeRegistryEntry<ModuleType<?>> {
    private final Function<ResourceLocation, T> loadValue;
    private String translationKey;
    private ITextComponent name;

    public ModuleType(Function<ResourceLocation, T> loadValue) {
        this.loadValue = loadValue;
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
}
