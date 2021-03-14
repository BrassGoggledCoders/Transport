package xyz.brassgoggledcoders.transport.screen.util;

import net.minecraft.util.text.*;

import java.util.function.Function;

public class FormattedLang {
    private final String translationKey;
    private Function<IFormattableTextComponent, ITextComponent> formatting = text -> text;

    public FormattedLang(String translationKey) {
        this.translationKey = translationKey;
    }

    public ITextComponent withArgs(Object... args) {
        return formatting.apply(new TranslationTextComponent(translationKey, args));
    }

    public FormattedLang withFormatting(TextFormatting... textFormatting) {
        this.formatting = text -> text.mergeStyle(textFormatting);
        return this;
    }
}
