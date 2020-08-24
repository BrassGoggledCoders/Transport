package xyz.brassgoggledcoders.transport.api.navigation;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiFunction;

public class NavigationPointType extends ForgeRegistryEntry<NavigationPointType> {
    private ITextComponent displayName;
    private String translationKey;
    private final BiFunction<NavigationPointType, World, NavigationPointInstance> instanceSupplier;

    public NavigationPointType(BiFunction<NavigationPointType, World, NavigationPointInstance> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("navigation_point", this.getRegistryName());
        }
        return this.translationKey;
    }

    public ITextComponent getDisplayName() {
        if (this.displayName == null) {
            this.displayName = new TranslationTextComponent(this.getTranslationKey());
        }
        return this.displayName;
    }
}
