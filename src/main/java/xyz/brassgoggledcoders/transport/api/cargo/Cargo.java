package xyz.brassgoggledcoders.transport.api.cargo;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.brassgoggledcoders.transport.api.cargo.instance.ICargoInstance;

import javax.annotation.Nullable;

public abstract class Cargo extends ForgeRegistryEntry<Cargo> {
    private String translationKey = null;

    public abstract ICargoInstance create(@Nullable World world);

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("cargo", this.getRegistryName());
        }
        return translationKey;
    }

    public ITextComponent getName() {
        return new TranslationTextComponent(translationKey);
    }
}
