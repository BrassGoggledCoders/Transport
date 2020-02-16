package xyz.brassgoggledcoders.transport.api.cargo.instance;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CargoInstanceEmpty implements ICargoInstance {
    private final static ITextComponent EMPTY_DESCRIPTION = new TranslationTextComponent(
            Util.makeTranslationKey("cargo", new ResourceLocation("transport", "empty"))
    );

    @Override
    public ITextComponent getDescription() {
        return EMPTY_DESCRIPTION;
    }
}
